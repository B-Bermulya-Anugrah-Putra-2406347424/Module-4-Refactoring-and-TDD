package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    CarRepository carRepository;

    @Mock
    IdGeneratorService idGenerator;

    @InjectMocks
    CarServiceImpl carService;

    Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("123");
        car.setCarName("Tesla");
    }

    @Test
    void testCreateWithNullId() {
        Car newCar = new Car();
        newCar.setCarName("Dodge");
        when(idGenerator.generateId()).thenReturn("generated-id-lol");
        Car created = carService.create(newCar);
        assertEquals("generated-id-lol", created.getCarId());
        verify(idGenerator, times(1)).generateId();
        verify(carRepository, times(1)).create(newCar);
    }

    @Test
    void testCreate() {
        when(carRepository.create(car)).thenReturn(car);
        Car created = carService.create(car);
        assertEquals(car, created);
        verify(idGenerator, never()).generateId();
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Iterator<Car> iterator = carList.iterator();

        when(carRepository.findAll()).thenReturn(iterator);
        List<Car> result = carService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindById() {
        when(carRepository.findById("123")).thenReturn(car);
        Car found = carService.findById("123");
        assertEquals(car, found);
    }

    @Test
    void testUpdate() {
        carService.update("123", car);
        verify(carRepository, times(1)).update("123", car);
    }

    @Test
    void testDelete() {
        carService.deleteCarById("123");
        verify(carRepository, times(1)).delete("123");
    }
}