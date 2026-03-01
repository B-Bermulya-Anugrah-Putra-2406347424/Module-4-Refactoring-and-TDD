package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepositoryImpl();
    }

    @Test
    void testCreateAndFindAll() {
        Car car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);
        carRepository.create(car);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());
        Car savedCar = carIterator.next();
        assertEquals(car.getCarId(), savedCar.getCarId());
    }

    @Test
    void testFindById() {
        Car car = new Car();
        car.setCarId("123");
        carRepository.create(car);

        Car found = carRepository.findById("123");
        assertEquals(car, found);

        Car notFound = carRepository.findById("random-ahh-car");
        assertNull(notFound);
    }

    @Test
    void testUpdate() {
        Car car = new Car();
        car.setCarId("123");
        car.setCarName("Old Car");
        carRepository.create(car);

        Car updatedInfo = new Car();
        updatedInfo.setCarName("New Car");
        updatedInfo.setCarColor("Red");
        updatedInfo.setCarQuantity(5);

        Car result = carRepository.update("123", updatedInfo);
        assertNotNull(result);
        assertEquals("New Car", car.getCarName());

        Car failResult = carRepository.update("nice-looking-id", updatedInfo);
        assertNull(failResult);
    }

    @Test
    void testDelete() {
        Car car = new Car();
        car.setCarId("123");
        carRepository.create(car);

        carRepository.delete("123");
        assertNull(carRepository.findById("123"));
    }
}