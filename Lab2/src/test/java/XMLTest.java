

import my.Constants;
import my.XML.Medicines;
import my.controller.DOMController;
import my.controller.SAXController;
import my.controller.STAXController;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class XMLTest {
    private static Medicines medicines;
    @BeforeClass
    public static void init() {
        medicines = new Medicines();
        Medicines.Medicine.Versions versions1 = new Medicines.Medicine.Versions(new ArrayList<>());
        versions1.getVersionList().add(new Medicines.Medicine.Versions.Version(
                new Medicines.Medicine.Versions.Version.Certificate("13435.DF54565.158","2020-01", "2021-01"),
                new Medicines.Medicine.Versions.Version.Package("Gel", 6,"123$" ),
                "200mg"
                ));

        Medicines.Medicine.Versions versions2 = new Medicines.Medicine.Versions(new ArrayList<>());
        versions2.getVersionList().add(new Medicines.Medicine.Versions.Version(
                new Medicines.Medicine.Versions.Version.Certificate("1255.DF54565.157","2019-01", "2021-01"),
                new Medicines.Medicine.Versions.Version.Package("Pill", 12,"243$" ),
                "200mg"
        ));
        versions2.getVersionList().add(new Medicines.Medicine.Versions.Version(
                new Medicines.Medicine.Versions.Version.Certificate("1255.DF54565.158","2020-01", "2021-01"),
                new Medicines.Medicine.Versions.Version.Package("Capsule", 12,"234$" ),
                "100mg"
        ));



        medicines.getMedicineList().add(new Medicines.Medicine("Ibuprofen", "Med-corp",
                "антибиотики", List.of(new String[]{"First Paracetamol",
                "Second Paracetamol"}),versions2
                ));
        medicines.getMedicineList().add(new Medicines.Medicine("Not Ibuprofen", "Mega-corp",
                "болеутоляющие", List.of(new String[]{"Not Paracetamol"}),versions1
        ));

    }

    @Test
    public void parseXml_DOM() {
        DOMController controller = new DOMController(Constants.INPUT_XML);
        controller.parseXML(true);
        controller.sortXML();
        assertEquals(medicines.getMedicineList(), controller.getMedicineList());
    }
    @Test
    public void parseXml_DOM_invalid() {
        DOMController controller = new DOMController(Constants.INVALID_PATH);
        controller.parseXML(true);
        assertNull(controller.getMedicineList());
    }

     @Test
    public void parseXml_SAX_invalid() {
        SAXController controller = new SAXController(Constants.INVALID_PATH);
        controller.parse(true);
         assertNull(controller.getMedicines());
    }
    @Test
    public void parseXml_STAX() {
        STAXController controller = new STAXController(Constants.INPUT_XML);
        controller.parse();
        controller.sortXML();
        assertEquals(medicines.getMedicineList(), controller.getMedicines());
    }
    @Test
    public void parseXml_STAX_invalid() {
        STAXController controller = new STAXController(Constants.INVALID_PATH);
        controller.parse();
        assertNull(controller.getMedicines());
    }

}