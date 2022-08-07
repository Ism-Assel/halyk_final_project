package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.models.enums.OrderStatus;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.OrderRepository;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.services.OrderService;
import kz.halykacademy.bookstore.utils.convertor.OrderConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final String MESSAGE_NOT_FOUND = "Order is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This order is existed";
    private final String MESSAGE_ORDER_PRICE = "Order price should not be greater than 10000 tg. Total price of order is: %.2f";
    private final Double MAX_PRICE = 10000.0;
    private final String MESSAGE_USER_NOT_FOUND = "User is not found with id = %d";
    private final String MESSAGE_IS_BLOCKED = "User is blocked";

    private final OrderRepository orderRepository;
    private final OrderConvertor orderConvertor;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderConvertor orderConvertor,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderConvertor = orderConvertor;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity create(OrderDTO orderDTO) {
        // проверка параметров запроса
        checkParameters(orderDTO);

        // конвертирование DTO в Entity
        Order order = orderConvertor.convertToOrder(orderDTO);

        // проверка общей суммы заказа и возращение списка книг
        List<Book> books = checkOrderPriceAndGetBooks(orderDTO);

        // проверка user на блокировку
        User user = checkUser(orderDTO);

        order.setBooks(books);
        order.setUser(user);
        order.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));

        orderRepository.save(order);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ModelResponseDTO(MESSAGE_SUCCESS));
    }

    @Override
    public ResponseEntity readById(Long id) {
        // Поиск order по id
        Optional<Order> orderById = orderRepository.findById(id);

        if (orderById.isEmpty()) {
            // Если не найден order
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        Order order = orderById.get();

        OrderDTO orderDTO = orderConvertor.convertToOrderDTO(orderById.get());
        List<Long> ids = new ArrayList<>();
        order.getBooks().forEach(book -> ids.add(order.getId()));

        orderDTO.setBooksId(ids);

        return new ResponseEntity(orderDTO, HttpStatus.OK);
    }

    @Override
    public List<OrderDTO> readAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderConvertor::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, OrderDTO updatedOrderDTO) {
        // проверка параметров запроса
        checkParameters(id, updatedOrderDTO);

        // Поиск order по id
        Optional<Order> orderById = orderRepository.findById(id);

        if (orderById.isPresent()) {
            // Если найден, обновляем order
            orderRepository.save(orderConvertor.convertToOrder(updatedOrderDTO));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
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
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(Long id, OrderDTO orderDTO) {
        notNull(id, "Id is undefined");
        checkParameters(orderDTO);
    }

    protected void checkParameters(OrderDTO orderDTO) {
        notNull(orderDTO.getStatus(), "Status is undefined");

        if (orderDTO.getBooksId().isEmpty()) {
            throw new ClientBadRequestException("List of books id is empty");
        }
        notNull(orderDTO.getUserId(), "User id is empty");
    }

    protected List<Book> checkOrderPriceAndGetBooks(OrderDTO orderDTO) {
        double totalPrice = 0;

        List<Book> books = bookRepository.findBookByIdIn(orderDTO.getBooksId());
        for (Book book : books) {
            totalPrice += book.getPrice();
        }

        if (totalPrice > MAX_PRICE) {
            throw new ClientBadRequestException(String.format(MESSAGE_ORDER_PRICE, totalPrice));
        }

        return books;
    }

    protected User checkUser(OrderDTO orderDTO) {
        Optional<User> user = userRepository.findById(orderDTO.getUserId());

        if (user.isEmpty()) {
            throw new ClientBadRequestException(String.format(MESSAGE_USER_NOT_FOUND, orderDTO.getUserId()));
        }

        if (user.get().getIsBlocked()) {
            throw new ClientBadRequestException(MESSAGE_IS_BLOCKED);
        }

        return user.get();
    }
}
