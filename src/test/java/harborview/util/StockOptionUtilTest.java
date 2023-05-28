package harborview.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StockOptionUtilTest {

    private static Stream<Arguments> optionSeriesSource() {
        return Stream.of(
                Arguments.of("3G", LocalDate.of(2023,7,21)),
                Arguments.of("3S", LocalDate.of(2023,7,21)),
                Arguments.of("3H", LocalDate.of(2023,8,18)),
                Arguments.of("3T", LocalDate.of(2023,8,18)),
                Arguments.of("3I", LocalDate.of(2023,9,15)),
                Arguments.of("3U", LocalDate.of(2023,9,15)),
                Arguments.of("3J", LocalDate.of(2023,10,20)),
                Arguments.of("3V", LocalDate.of(2023,10,20)),
                Arguments.of("3K", LocalDate.of(2023,11,17)),
                Arguments.of("3W", LocalDate.of(2023,11,17)),
                Arguments.of("3L", LocalDate.of(2023,12,15)),
                Arguments.of("3X", LocalDate.of(2023,12,15))
        );
    }

    @ParameterizedTest
    @MethodSource("optionSeriesSource")
    void testSeriesAsDate(String series, LocalDate expected) {
        assertThat(StockOptionUtil.seriesAsDate(series)).isEqualTo(expected);
    }

}
