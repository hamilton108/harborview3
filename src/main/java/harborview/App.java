package harborview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

//@EnableCaching
@SpringBootApplication
@ComponentScan({"oahu.properties"
		, "harborview.controller"
		, "harborview.core"
		, "harborview.adapter"
		, "harborview.mybatis"
		, "vega.financial.calculator"
})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
