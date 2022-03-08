/**
 * Tests for CarSerDe
 * 
 * @author Björn Beskow
 */
package se.callista.blog.avro_spring.car.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import se.callista.blog.avro_spring.car.avro.Car;
import se.callista.blog.avro_spring.car.avro.serde.CarSerDe;
import se.callista.blog.avro_spring.car.persist.CarRepository;
import se.callista.blog.avro_spring.serde.AvroConstants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Tests for CarClient
 * 
 * @author Björn Beskow
 */
@ExtendWith(SpringExtension.class)
@RestClientTest(CarClient.class)
@ComponentScan("se.callista.blog.avro_spring.car")
public class CarClientTest {

  private static final String VIN = "123456789";
  private static final String PLATE_NUMBER = "ABC 123";
  private static final String MEDIA_SUB_TYPE_BINARY = AvroConstants.MEDIA_SUBTYPE_AVRO_BINARY;
  private static final String MEDIA_SUB_TYPE_NON_BINARY = AvroConstants.MEDIA_SUBTYPE_AVRO_JSON;
  private static final String MEDIA_TYPE_APPLICATION = AvroConstants.MEDIA_TYPE;

  private static CarSerDe carSerDeBinary = new CarSerDe(true);
  private static CarSerDe carSerDeNonBinary = new CarSerDe(false);

  private static Car car;
  private static byte[] serializedCarBinary;
  private static byte[] serializedCarNonBinary;

  @Autowired
  private CarClient client;

  @Autowired
  private MockRestServiceServer server;

  @MockBean
  private CarRepository carRepository;

  @BeforeAll
  public static void setUp() throws Exception {
    car = new Car(VIN, PLATE_NUMBER);
    serializedCarBinary = carSerDeBinary.serialize(car);
    serializedCarNonBinary = carSerDeNonBinary.serialize(car);
  }

  @Test
  public void testGetCarBinary() throws Exception {
    given(carRepository.getCar(VIN)).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(serializedCarBinary, new MediaType(MEDIA_TYPE_APPLICATION, MEDIA_SUB_TYPE_BINARY)));

    Car actualCar = this.client.getCar(VIN);

    assertThat(actualCar).isEqualTo(car);
  }

  @Test
  public void testGetCarNonBinary() throws Exception {
    given(carRepository.getCar(VIN)).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(serializedCarNonBinary, new MediaType(MEDIA_TYPE_APPLICATION, MEDIA_SUB_TYPE_NON_BINARY)));

    Car actualCar = this.client.getCar(VIN);

    assertThat(actualCar).isEqualTo(car);
  }

  @Test
  public void testUpdateCarBinary() throws Exception {
    given(carRepository.updateCar(any(Car.class))).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.PUT))
        .andRespond(withSuccess(serializedCarBinary, new MediaType(MEDIA_TYPE_APPLICATION, MEDIA_SUB_TYPE_BINARY)));

    Car actualCar = this.client.updateCar(VIN, car);

    assertThat(actualCar).isEqualTo(car);
  }

  @Test
  public void testUpdateCarNonBinary() throws Exception {
    given(carRepository.updateCar(any(Car.class))).willReturn(car);
    this.server.expect(requestTo("/car/" + VIN)).andExpect(method(HttpMethod.PUT))
        .andRespond(withSuccess(serializedCarNonBinary, new MediaType(MEDIA_TYPE_APPLICATION, MEDIA_SUB_TYPE_NON_BINARY)));

    Car actualCar = this.client.updateCar(VIN, car);

    assertThat(actualCar).isEqualTo(car);
  }
}
