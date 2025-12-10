package es.upm.grise.profundizacion.order;


import es.upm.grise.exceptions.IncorrectItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import es.upm.grise.profundizacion.order.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderTest {


    private Order order;
    private Product productMock;

    @BeforeEach
    void setUp() {
        order = new Order();
        productMock = mock(Product.class);
        when(productMock.getId()).thenReturn(1L); // ahora devuelve long
    }

    @Test
    void testOrderStartsEmpty() {
        assertNotNull(order.getItems());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testAddValidItem() throws IncorrectItemException {
        Item item = mock(Item.class);
        when(item.getProduct()).thenReturn(productMock);
        when(item.getPrice()).thenReturn(1000.0);
        when(item.getQuantity()).thenReturn(1);

        order.addItem(item);

        Collection<Item> items = order.getItems();
        assertEquals(1, items.size());
        assertTrue(items.contains(item));
    }

    @Test
    void testAddItemWithNegativePriceThrowsException() {
        Item item = mock(Item.class);
        when(item.getProduct()).thenReturn(productMock);
        when(item.getPrice()).thenReturn(-10.0);
        when(item.getQuantity()).thenReturn(1);

        assertThrows(IncorrectItemException.class, () -> order.addItem(item));
    }

    @Test
    void testAddItemWithZeroQuantityThrowsException() {
        Item item = mock(Item.class);
        when(item.getProduct()).thenReturn(productMock);
        when(item.getPrice()).thenReturn(100.0);
        when(item.getQuantity()).thenReturn(0);

        assertThrows(IncorrectItemException.class, () -> order.addItem(item));
    }

    @Test
    void testAddItemSameProductSamePriceIncrementsQuantity() throws IncorrectItemException {
        Item item1 = mock(Item.class);
        when(item1.getProduct()).thenReturn(productMock);
        when(item1.getPrice()).thenReturn(500.0);
        when(item1.getQuantity()).thenReturn(1);

        Item item2 = mock(Item.class);
        when(item2.getProduct()).thenReturn(productMock);
        when(item2.getPrice()).thenReturn(500.0);
        when(item2.getQuantity()).thenReturn(2);

        order.addItem(item1);
        order.addItem(item2);

        // Verificamos que se llamó a setQuantity en el item existente
        verify(item1).setQuantity(3);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testAddItemSameProductDifferentPriceAddsNewItem() throws IncorrectItemException {
        Item item1 = mock(Item.class);
        when(item1.getProduct()).thenReturn(productMock);
        when(item1.getPrice()).thenReturn(500.0);
        when(item1.getQuantity()).thenReturn(1);

        Item item2 = mock(Item.class);
        when(item2.getProduct()).thenReturn(productMock);
        when(item2.getPrice()).thenReturn(600.0);
        when(item2.getQuantity()).thenReturn(1);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(2, order.getItems().size());
        assertTrue(order.getItems().contains(item1));
        assertTrue(order.getItems().contains(item2));
    }
}

