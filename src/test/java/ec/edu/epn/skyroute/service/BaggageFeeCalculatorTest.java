package ec.edu.epn.skyroute.service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)


public class BaggageFeeCalculatorTest {

    @Mock
    private PassengerService paseengerService;

    @InjectMocks
    private BaggageFeeCalculator baggageFeeCalculator;

    @Test
    @DisplayName("Calcular tarifa base por cada maleta")
    public void testCalcularTarifaBaseEquipaje(){
        double weight = 20.0; // En este punto se agrega un peso que no exceda el limite de 23kg
        int bagCount = 1; // En este punto se agrega una maleta para la tarifa basica
        Long pasengerId= 1L;
        double tarifa = baggageFeeCalculator.calculateFee(weight,bagCount,pasengerId);
        assertEquals(30,tarifa);
    }

    @Test
    @DisplayName("Calcular la tarifa por exceso de peso")
    public void testCalcularTarifaPorPesoExcesivo(){
        double weight = 25; // En este punto se agrega un peso que exceda el limite de 23kg
        int bagCount = 1;
        Long pasengerId= 1L;
        double tarifa = baggageFeeCalculator.calculateFee(weight,bagCount,pasengerId);
        assertEquals(80,tarifa);
    }

    @Test
    @DisplayName("Calcular tarifa para pasajero VIP sin exceder peso")
    public void testCalcularTarifaConPasajeroVIP(){
        double weight = 15;
        int bagCount = 1;
        Long passengerId =1L;
        when(paseengerService.isVip(passengerId)).thenReturn(true); // En este punto se simula que es un pasajero VIP
        double tarifa = baggageFeeCalculator.calculateFee(weight,bagCount,passengerId);
        assertEquals(0,tarifa);
    }

    @Test
    @DisplayName("Calcular tarifa para pasajero VIP con mas de una maleta")
    public void testCalcularTarifaConPasajeroVIPCasoLimite(){
        double weight = 15;
        int bagCount = 2; // Se cambia el numero de maletas por 2
        Long passengerId =1L;
        when(paseengerService.isVip(passengerId)).thenReturn(true); // En este punto se simula que es un pasajero VIP
        double tarifa = baggageFeeCalculator.calculateFee(weight,bagCount,passengerId);
        assertEquals(30,tarifa);
    }

    @Test
    @DisplayName("Validar la excepcion del peso invalido")
    public void testRevisarPesoInvalido(){
        double weight =-5; //Se agrega un peso invalido
        int bagCount =1;
        Long passengerId = 1L;
        assertThrows(IllegalArgumentException.class , () -> baggageFeeCalculator.calculateFee(weight,bagCount,passengerId));
    }
}
