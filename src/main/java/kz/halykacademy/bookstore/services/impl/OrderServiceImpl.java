package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.order.OrderAdminRequest;
import kz.halykacademy.bookstore.dto.order.OrderUserRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.models.enums.OrderStatus;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.OrderRepository;
import kz.halykacademy.bookstore.services.OrderService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
import kz.halykacademy.bookstore.utils.convertor.OrderConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;
import static kz.halykacademy.bookstore.utils.MessageInfo.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderConvertor orderConvertor;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            BookRepository bookRepository,
            OrderConvertor orderConvertor
    ) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.orderConvertor = orderConvertor;
    }

    @Override
    public ResponseEntity create(OrderUserRequest request) {
        try {
            // проверяем заблокирован ли пользователь
            User user = BlockedUserChecker.checkBlockedUser();

            // проверка параметров запроса
            checkParameters(request);

            // подготовка заказа
            Order order = prepareOrder(request, null, user);

            // сохранение заказа
            order = orderRepository.save(order);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(orderConvertor.toOrderDto(order));

        } catch (Exception e) {
            throw e;
        }
    }

    public ResponseEntity readById(Long id) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Поиск order по id
            Optional<Order> foundOrder = orderRepository.findById(id);

            if (foundOrder.isEmpty()) {
                // Если не найден order
                throw new ResourceNotFoundException(String.format(MESSAGE_ORDER_NOT_FOUND, id));
            }

            return new ResponseEntity(foundOrder.map(orderConvertor::toOrderDto).get(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readAll() {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Поиск заказов в БД
            List<Order> orders = orderRepository.findAll();
            if (orders.isEmpty()) {
                // Если не найден, выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_ORDERS);
            }

            return new ResponseEntity(
                    orders.stream()
                            .map(orderConvertor::toOrderDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity update(Long id, OrderUserRequest request) {
        try {
            // проверяем заблокирован ли пользователь
            User user = BlockedUserChecker.checkBlockedUser();

            // проверка параметров запроса
            checkParameters(id, request);

            // Поиск order по id
            Optional<Order> foundOrder = orderRepository.findById(id);

            if (foundOrder.isPresent()) {
                // Если найден, обновляем order
                Order order = prepareOrder(request, foundOrder.get(), user);

                order = orderRepository.save(order);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(orderConvertor.toOrderDto(order));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_ORDER_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        try {
            // Проверка параметра id
            notNull(id, "Id is undefined");

            // Поиск order по id
            Optional<Order> foundOrder = orderRepository.findById(id);

            if (foundOrder.isPresent()) {
                // если нашли, удаляем
                orderRepository.deleteById(id);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ModelResponseDTO(MESSAGE_SUCCESS));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_ORDER_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    protected void checkParameters(Long id, OrderUserRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(OrderUserRequest request) {
        // проверка запроса на соответствие типу данных OrderAdminRequest
        if (request instanceof OrderAdminRequest) {
            OrderAdminRequest adminRequest = (OrderAdminRequest) request;
            notNull(adminRequest.getStatus(), "Status is undefined");
        }

        // проверка на список книг в заказе
        if (request.getBooksId().isEmpty()) {
            throw new ClientBadRequestException("List of books id is empty");
        }
    }

    protected List<Book> checkOrderPriceAndGetBooks(OrderUserRequest request) {
        double totalPrice = 0;

        // поиск книг по Id
        List<Book> books = bookRepository.findBookByIdIn(request.getBooksId());
        for (Book book : books) {
            // проходим через каждую книгу и инкрементим цену книг
            totalPrice += book.getPrice();
        }

        if (totalPrice > MAX_PRICE_ORDER) {
            throw new ClientBadRequestException(String.format(MESSAGE_ORDER_PRICE, totalPrice));
        }

        return books;
    }

    protected Order prepareOrder(OrderUserRequest request, Order order, User user) {
        OrderAdminRequest adminRequest = null;
        boolean isAdmin = false;

        // проверка запроса на соответствие типу данных OrderAdminRequest
        if (request instanceof OrderAdminRequest) {
            adminRequest = (OrderAdminRequest) request;
            isAdmin = true;
        }

        // определение принадлежит ли заказ к текущему пользователю
        if (order != null
                && user.getRole().equals("ROLE_USER")
                && !order.getUser().getId().equals(user.getId())
        ) {
            throw new ClientBadRequestException("This order doesn't access to the current user");
        }

        // определение статуса заказа для User/Admin
        OrderStatus status;
        if (isAdmin) {
            status = OrderStatus.valueOf(adminRequest.getStatus());
        } else {
            status = order != null ? order.getStatus() : OrderStatus.CREATED;
        }

        // проверка общей суммы заказа и возращение списка книг
        List<Book> books = checkOrderPriceAndGetBooks(request);

        return new Order(
                request.getId(),
                books,
                status,
                order != null ? order.getCreatedAt() : null,
                order != null ? order.getUser() : user
        );
    }
}