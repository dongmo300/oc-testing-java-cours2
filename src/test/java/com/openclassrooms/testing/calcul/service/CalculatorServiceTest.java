package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

	@Mock
	Calculator calculator ;
	@Mock
    SolutionFormatter solutionFormatter;
	// Calculator IS CALLED BY CalculatorService
	@InjectMocks
	CalculatorServiceImpl classUnderTest ;

	@Test
	public void add_returnsTheSum_ofTwoPositiveNumbers() {
		
		//Arrange : definir la réponse du mock
		when(calculator.add(1, 2)).thenReturn(3);
		
		//act : appeler le service, la classe à tester
		final int result = classUnderTest.calculate(
				new CalculationModel(CalculationType.ADDITION, 1, 2)).getSolution();
		
		//Assert : vérifier l'interaction et le résultat
		verify(calculator).add(1, 2);
		assertThat(result).isEqualTo(3);
	}

	@Test
	public void add_returnsTheSum_ofTwoNegativeNumbers() {
		//Arrange
		when(calculator.add(-1, 2)).thenReturn(1);
		//when
		final int result = classUnderTest.calculate(
				new CalculationModel(CalculationType.ADDITION, -1, 2))
				.getSolution();
		//then
		verify(calculator).add(-1, 2);
		assertThat(result).isEqualTo(1);
	}
	
	@Test
	public void calculate_shouldUseCalculator_forAnyAddition() {
		//Geven
		Random r = new Random();
		when(calculator.add(any(Integer.class), any(Integer.class))).thenReturn(3);
		//act
		final int resulta = classUnderTest.calculate(
				new CalculationModel(CalculationType.ADDITION, r.nextInt(), r.nextInt())).getSolution();
		//assert
		verify(calculator , times(1)).add(any(Integer.class), any(Integer.class));
		verify(calculator , times(0)).sub(any(Integer.class), any(Integer.class));
		verify(calculator , never()).multiply(any(Integer.class), any(Integer.class));
		assertThat(resulta).isEqualTo(3);
	}
	
	@Test
	public void calculate_shouldThrowIllegalArgumentException_forADivisionBy0() {
		//geven
		when(calculator.divide(1, 0)).thenThrow(new IllegalArgumentException());
		//when
		assertThrows(IllegalArgumentException.class, () -> classUnderTest.calculate(
				new CalculationModel(
						CalculationType.DIVISION, 1, 0)));
		//assert
		verify(calculator, times(1)).divide(1, 0);
		
	}
	
	@Test
    public void calculate_shouldFormatSolution_forAnAddition() {
        // GIVEN
        when(calculator.add(10000, 3000)).thenReturn(13000);
        when(solutionFormatter.format(13000)).thenReturn("13 000");

        // WHEN
        final String formattedResult = classUnderTest
            .calculate(new CalculationModel(CalculationType.ADDITION, 10000, 3000))
            .getFormattedSolution();

        // THEN
        assertThat(formattedResult).isEqualTo("13 000");
    }

	
}
