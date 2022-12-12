package my.controller;

import lombok.Getter;
import my.Constants;
import my.XML.Medicines;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DOMController {
    private final String filename;

    @Getter
    private List<Medicines.Medicine> medicineList;

    public DOMController(String filename) {
        medicineList = new ArrayList<>();
        this.filename = filename;
    }
    public void parseXML(boolean validate){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            dbf.setNamespaceAware(true);
            if (validate) {
                dbf.setFeature(Constants.FEATURE_TURN_VALIDATION_ON, true);
                dbf.setFeature(Constants.FEATURE_TURN_SCHEMA_VALIDATION_ON, true);
            }

            Document doc = db.parse(new File(filename));

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName(Constants.MEDICINE);

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);
                Medicines.Medicine medicine;

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    String name = element.getElementsByTagName(Constants.NAME).item(0).getTextContent();
                    String pharm = element.getElementsByTagName(Constants.PHARM).item(0).getTextContent();
                    String group = element.getElementsByTagName(Constants.GROUP).item(0).getTextContent();
                    List<String> analogs = Arrays.stream(element.getElementsByTagName(Constants.ANALOGS).item(0).getTextContent().split(Constants.SPLIT_REGEX)).map(String::strip).toList();
                    Node nodeListVersions = element.getElementsByTagName(Constants.VERSIONS).item(0);
                    Medicines.Medicine.Versions versions = getVersions(nodeListVersions);

                    medicine = new Medicines.Medicine(name, pharm, group, analogs, versions);
                    medicineList.add(medicine);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Logger.getLogger(DOMController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
            medicineList = null;
        }
    }

    private Medicines.Medicine.Versions getVersions(Node node) {
        Medicines.Medicine.Versions versions = new Medicines.Medicine.Versions();
        Element element = (Element) node;

        for (int tmp = 0; tmp < element.getElementsByTagName(Constants.VERSION).getLength(); tmp++) {
            Medicines.Medicine.Versions.Version version;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Node tmpNode = element.getElementsByTagName(Constants.VERSION).item(tmp);
                version = getVersion(tmpNode);
                versions.getVersionList().add(version);
            }
        }
        return versions;
    }

    private Medicines.Medicine.Versions.Version getVersion(Node node) {
        Medicines.Medicine.Versions.Version version = new Medicines.Medicine.Versions.Version();
        Medicines.Medicine.Versions.Version.Certificate certificate = new Medicines.Medicine.Versions.Version.Certificate();
        Medicines.Medicine.Versions.Version.Package aPackage = new Medicines.Medicine.Versions.Version.Package();
        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element element1 = (Element) node;
            certificate.setNumber(element1.getElementsByTagName(Constants.CERTIFICATE).item(0).getAttributes()
                    .getNamedItem(Constants.NUMBER).getTextContent());
            certificate.setDataFrom(element1.getElementsByTagName(Constants.CERTIFICATE).item(0).getAttributes()
                    .getNamedItem(Constants.DATA_FROM).getTextContent());
            certificate.setDataTo(element1.getElementsByTagName(Constants.CERTIFICATE).item(0).getAttributes()
                    .getNamedItem(Constants.DATA_TO).getTextContent());
            version.setCertificate(certificate);
            aPackage.setType(element1.getElementsByTagName(Constants.PACKAGE).item(0).getAttributes()
                    .getNamedItem(Constants.TYPE).getTextContent());
            aPackage.setNumberInPackage(Integer.parseInt(element1.getElementsByTagName(Constants.PACKAGE).item(0).getAttributes()
                    .getNamedItem(Constants.NUMBER_IN_PACKAGE).getTextContent()));
            aPackage.setPriceForPackage(element1.getElementsByTagName(Constants.PACKAGE).item(0).getAttributes()
                    .getNamedItem(Constants.PRICE_FOR_PACKAGE).getTextContent());
            version.setAPackage(aPackage);
            version.setDosage(element1.getElementsByTagName(Constants.DOSAGE).item(0).getTextContent());
        }
        return version;
    }
    public void sortXML(){
        Collections.sort(medicineList);
    }
    private Document createDocument() throws ParserConfigurationException {
        final String[] medicinesArray = { Constants.NAME, Constants.PHARM, Constants.GROUP, Constants.ANALOGS, Constants.VERSION};
        final String[] versionArray = { Constants.CERTIFICATE, Constants.PACKAGE, Constants.DOSAGE };
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        dbf.setNamespaceAware(true);
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement(Constants.MEDICINES);
        root.setAttribute("xmlns", "http://www.nure.ua");
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.setAttribute("xsi:schemaLocation", "http://www.nure.ua ../resources/input.xsd ");
        document.appendChild(root);
        Element element;
        for (Medicines.Medicine medicine : medicineList) {
            Element medicineElement = document.createElement(Constants.MEDICINE);
            List<Object> listedMedicines = medicineToList(medicine);
            //first 3 elements xml
            for (int i = 0; i < 3; i++) {
                element = document.createElement(medicinesArray[i]);
                element.setTextContent(listedMedicines.get(i).toString());
                medicineElement.appendChild(element);
            }
            //3 - is analogs
            element = document.createElement(medicinesArray[3]);
            element.setTextContent(String.join(",\n", medicine.getAnalogs()));
            medicineElement.appendChild(element);

            Element versions = document.createElement(Constants.VERSIONS);
            for (int i = 0; i < medicine.getVersions().getVersionList().size(); i++) {
                Element version = document.createElement(Constants.VERSION);
                Element childElement;

                //certificate
                childElement = document.createElement(versionArray[0]);
                childElement.setAttribute(Constants.NUMBER, medicine.getVersions().getVersionList().get(i).getCertificate().getNumber());
                childElement.setAttribute(Constants.DATA_FROM, medicine.getVersions().getVersionList().get(i).getCertificate().getDataFrom());
                childElement.setAttribute(Constants.DATA_TO, medicine.getVersions().getVersionList().get(i).getCertificate().getDataTo());
                version.appendChild(childElement);
                //package
                childElement = document.createElement(versionArray[1]);
                childElement.setAttribute(Constants.TYPE, medicine.getVersions().getVersionList().get(i).getAPackage().getType());
                childElement.setAttribute(Constants.NUMBER_IN_PACKAGE, String.valueOf(medicine.getVersions().getVersionList().get(i).getAPackage().getNumberInPackage()));
                childElement.setAttribute(Constants.PRICE_FOR_PACKAGE, medicine.getVersions().getVersionList().get(i).getAPackage().getPriceForPackage());
                version.appendChild(childElement);
                //dosage
                childElement = document.createElement(versionArray[2]);
                childElement.setTextContent(medicine.getVersions().getVersionList().get(i).getDosage());
                version.appendChild(childElement);

                versions.appendChild(version);
            }
            medicineElement.appendChild(versions);
            root.appendChild(medicineElement);
        }
        return document;

    }

    public void saveToXML(String outPath){

        StreamResult result = new StreamResult(new File(outPath));

        try {
            TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            javax.xml.transform.Transformer t;
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(createDocument()), result);
        } catch (ParserConfigurationException | TransformerException e) {
            Logger.getLogger(DOMController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }

    }

    public static List<Object> medicineToList(Medicines.Medicine medicine) {
        List<Object> resultList = new ArrayList<>();
        resultList.add(medicine.getName());
        resultList.add(medicine.getPharm());
        resultList.add(medicine.getGroup());
        resultList.add(medicine.getAnalogs());
        resultList.add(medicine.getVersions());
        return resultList;
    }
}
