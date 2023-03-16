package com.nobes.timetable;

import com.nobes.timetable.product.logic.ScrapService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TimetableApplicationTests {

	@Resource
	ScrapService service;

	@Test
	void scrapTest1() throws IOException {
		Integer catoid = 34;
		Integer poid = 38638;
		String term_Name = "Term 3";
		String url = "https://calendar.ualberta.ca/preview_program.php?catoid=" + catoid.toString() + "&poid=" + poid.toString();
		Document document = Jsoup.connect(url).get();
		ArrayList arrayList = service.getCourses(document).get(term_Name);
		List<String> expectcourses = Arrays.asList(
				"BIOL 107",
				"CH E 243",
				"CME 200",
				"CME 265",
				"CHEM 261",
				"Complementary Studies Elective (3-0-0)",
				"MATH 209");
		Assertions.assertEquals(arrayList,expectcourses);
	}

	@Test
	void scrapTest2() throws IOException {
		Integer catoid = 34;
		Integer poid = 38664;
		String term_Name = "Term 5";
		String url = "https://calendar.ualberta.ca/preview_program.php?catoid=" + catoid.toString() + "&poid=" + poid.toString();
		Document document = Jsoup.connect(url).get();
		ArrayList arrayList = service.getCourses(document).get(term_Name);
		List<String> expectcourses = Arrays.asList(
				"CH E 314",
				"CH E 343",
				"CH E 351",
				"CH E 374",
				"CH E 446",
				"ENGG 404");
		Assertions.assertEquals(arrayList,expectcourses);
	}

	@Test
	void scrapTest3() throws IOException {
		Integer catoid = 34;
		Integer poid = 38707;
		String term_Name = "Term 7";
		String url = "https://calendar.ualberta.ca/preview_program.php?catoid=" + catoid.toString() + "&poid=" + poid.toString();
		Document document = Jsoup.connect(url).get();
		ArrayList arrayList = service.getCourses(document).get(term_Name);
		List<String> expectcourses = Arrays.asList(
				"ECE 490",
				"ENGG 404",
				"Group I Program and Technical Elective",
				"Group II Program and Technical Elective",
				"Group II Program and Technical Elective",
				"Group II Program and Technical Elective");
		Assertions.assertEquals(arrayList,expectcourses);
	}

	@Test
	void aaa() {
		System.out.println("  CMPUT 243".replaceAll("\\s+", ""));
	}

}
