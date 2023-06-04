package com.bloom.api.dto;

import com.bloom.api.dto.address.AddressDTO;
import com.bloom.api.dto.address.AddressDTOMapper;
import com.bloom.api.dto.order.OrderDTO;
import com.bloom.api.dto.order.OrderDTOMapper;
import com.bloom.api.dto.product.ProductDTO;
import com.bloom.api.dto.product.ProductDTOMapper;
import com.bloom.api.dto.user.UserDTO;
import com.bloom.api.dto.user.UserDTOMapper;
import com.bloom.api.models.Address;
import com.bloom.api.models.Order;
import com.bloom.api.models.Product;
import com.bloom.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MappedDTO {
    private final UserDTOMapper userDTOMapper;
    private final AddressDTOMapper addressDTOMapper;
    private final ProductDTOMapper productDTOMapper;
    private final OrderDTOMapper orderDTOMapper;


    public List<UserDTO> mapUsersDTO(List<User> users) {
        return users.stream()
            .map(userDTOMapper)
            .collect(Collectors.toList());
    }

    public UserDTO mapUserDTO(User user) {
        return userDTOMapper.apply(user);
    }

    public AddressDTO mapAddressDTO(Address address) {
        return addressDTOMapper.apply(address);
    }

    public List<AddressDTO> mapAddressesDTO(List<Address> addresses) {
        return addresses.stream()
            .map(addressDTOMapper)
            .collect(Collectors.toList());
    }

    public ProductDTO mapProductDTO(Product product) {
        return productDTOMapper.apply(product);
    }

    public List<ProductDTO> mapProductsDTO(List<Product> products) {
        return products.stream()
            .map(productDTOMapper)
            .collect(Collectors.toList());
    }

    public OrderDTO mapOrderDTO(Order order) {
        return orderDTOMapper.apply(order);
    }

    public List<OrderDTO> mapOrdersDTO(List<Order> orders) {
        return orders.stream()
            .map(orderDTOMapper)
            .collect(Collectors.toList());
    }
}
