/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.callista.blog.avro_spring.car.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.car.avro.serde.CarSerDe;
import se.callista.blog.avro_spring.car.persist.CarRepository;
import se.callista.blog.avro_spring.serde.AvroConstants;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for CarController
 * 
 * @author Björn Beskow
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
@AutoConfigureWebClient
public class CarControllerTest {

  private static final String VIN = "123456789";
  private static final String PLATE_NUMBER = "ABC 123";
  private static final String MEDIA_TYPE_BINARY = AvroConstants.MEDIA_TYPE_AVRO_BINARY;
  private static final String MEDIA_TYPE_NON_BINARY = AvroConstants.MEDIA_TYPE_AVRO_JSON;

  private Car car;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private CarRepository carRepository;

  @BeforeEach
  public void setUp() throws Exception {
    car = new Car(VIN, PLATE_NUMBER);

    given(carRepository.getCar(VIN)).willReturn(car);
    given(carRepository.updateCar(any(Car.class))).willReturn(car);
  }

  @Test
  public void testGetCarNonBinar() throws Exception {
    CarSerDe carSerDe = new CarSerDe(false);
    byte[] serializedCar = carSerDe.serialize(car);
    mvc.perform(get("/car/" + VIN).accept(MEDIA_TYPE_NON_BINARY)).andExpect(status().isOk())
        .andExpect(content().contentType(MEDIA_TYPE_NON_BINARY))
        .andExpect(content().bytes(serializedCar));
  }

  @Test
  public void testGetCarBinary() throws Exception {
    CarSerDe carSerDe = new CarSerDe(true);
    byte[] serializedCar = carSerDe.serialize(car);
    mvc.perform(get("/car/" + VIN).accept(MEDIA_TYPE_BINARY)).andExpect(status().isOk())
        .andExpect(content().contentType(MEDIA_TYPE_BINARY))
        .andExpect(content().bytes(serializedCar));
  }

  @Test
  public void testUpdateCarNonBinary() throws Exception {

    CarSerDe carSerDe = new CarSerDe(false);
    byte[] serializedCar = carSerDe.serialize(car);

    mvc.perform(put("/car/" + VIN).accept(MEDIA_TYPE_NON_BINARY).contentType(MEDIA_TYPE_NON_BINARY)
            .content(serializedCar)).andExpect(status().isOk())
        .andExpect(content().contentType(MEDIA_TYPE_NON_BINARY))
        .andExpect(content().bytes(serializedCar));
  }

  @Test
  public void testUpdateCarBinary() throws Exception {

    CarSerDe carSerDe = new CarSerDe(true);
    byte[] serializedCar = carSerDe.serialize(car);


    mvc.perform(put("/car/" + VIN).accept(MEDIA_TYPE_BINARY).contentType(MEDIA_TYPE_BINARY)
            .content(serializedCar)).andExpect(status().isOk())
        .andExpect(content().contentType(MEDIA_TYPE_BINARY))
        .andExpect(content().bytes(serializedCar));
  }
}
