package my.controller;

import lombok.Getter;
import my.Constants;
import my.XML.Medicines;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SAXController extends DefaultHandler {

    private final String filename;

    @Getter
    private final List<Medicines.Medicine> medicines;
    private final Deque<Object> elementDeque;
    private final Deque<Medicines.Medicine> objectDeque;
    private final Deque<Medicines.Medicine.Versions.Version> versionObjectDeque;
    //private Deque<Medicines.Medicine.GrowingTips> gtObjectDeque;

    public SAXController(String filename) {
        medicines = new ArrayList<>(20);
        elementDeque = new ArrayDeque<>();
        objectDeque = new ArrayDeque<>();
        versionObjectDeque = new ArrayDeque<>();
        //gtObjectDeque = new ArrayDeque<>();
        this.filename = filename;
    }

    public void parse(boolean validate) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (ParserConfigurationException | SAXNotRecognizedException | SAXNotSupportedException e) {
            Logger.getLogger(DOMController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }

        if (validate) {
            factory.setValidating(true);
            try {
                factory.setFeature(Constants.FEATURE_TURN_VALIDATION_ON, true);
                factory.setFeature(Constants.FEATURE_TURN_SCHEMA_VALIDATION_ON, true);
            } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
                Logger.getLogger(DOMController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
            }
        }
        javax.xml.parsers.SAXParser parser;
        try {
            parser = factory.newSAXParser();
            parser.parse(filename, this);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            Logger.getLogger(SAXController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }
    }

    public void sortXML(){
        Collections.sort(medicines);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){

        this.elementDeque.push(qName);

        if (Constants.MEDICINE.equals(qName)) {
            Medicines.Medicine Medicine = new Medicines.Medicine();
            this.objectDeque.push(Medicine);
        } else if (Constants.VERSION.equals(qName)) {
            Medicines.Medicine.Versions.Version version1 = new Medicines.Medicine.Versions.Version();
            this.versionObjectDeque.push(version1);
        } else if(Constants.CERTIFICATE.equals(currentElement())){
            Medicines.Medicine.Versions.Version version = this.versionObjectDeque.peek();
            Medicines.Medicine.Versions.Version.Certificate certificate = new Medicines.Medicine.Versions.Version.Certificate();
            certificate.setNumber(attributes.getValue(Constants.NUMBER));
            certificate.setDataFrom(attributes.getValue(Constants.DATA_FROM));
            certificate.setDataTo(attributes.getValue(Constants.DATA_TO));
            Objects.requireNonNull(version).setCertificate(certificate);
        } else if(Constants.PACKAGE.equals(currentElement())){
            Medicines.Medicine.Versions.Version version = this.versionObjectDeque.peek();
            Medicines.Medicine.Versions.Version.Package aPackage = new Medicines.Medicine.Versions.Version.Package();
            aPackage.setType(attributes.getValue(Constants.TYPE));
            aPackage.setNumberInPackage(Integer.parseInt(attributes.getValue(Constants.NUMBER_IN_PACKAGE)));
            aPackage.setPriceForPackage(attributes.getValue(Constants.PRICE_FOR_PACKAGE));
            Objects.requireNonNull(version).setAPackage(aPackage);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName){

        this.elementDeque.pop();

        if (Constants.MEDICINE.equals(qName)) {
            Medicines.Medicine object = this.objectDeque.pop();
            this.medicines.add(object);
        } else if (Constants.VERSIONS.equals(qName)) {
            Medicines.Medicine Medicine = this.objectDeque.peek();
            //Medicines.Medicine.Versions.Version object = this.versionObjectDeque;
            Objects.requireNonNull(Medicine).setVersions(new Medicines.Medicine.Versions(this.versionObjectDeque.stream().toList()));
            this.versionObjectDeque.clear();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length){
        String value = new String(ch, start, length);
        if (value.length() == 0) {
            return;
        }

        Medicines.Medicine Medicine = this.objectDeque.peek();

        if (Constants.NAME.equals(currentElement())) {
            Objects.requireNonNull(Medicine).setName(value);
        } else if (Constants.PHARM.equals(currentElement())) {
            Objects.requireNonNull(Medicine).setPharm(value);
        } else if (Constants.GROUP.equals(currentElement())){
            Objects.requireNonNull(Medicine).setGroup(value);
        }
        else if(Constants.ANALOGS.equals(currentElement())){
            Objects.requireNonNull(Medicine).setAnalogs(Arrays.stream(value.split(Constants.SPLIT_REGEX)).toList());
        }
        else if(Constants.DOSAGE.equals(currentElement())){
            Medicines.Medicine.Versions.Version visualParameters = this.versionObjectDeque.peek();
            Objects.requireNonNull(visualParameters).setDosage(value);
        }
    }

    private Object currentElement()
    {
        return this.elementDeque.peek();
    }

    public void writeXML(String outPath){
        try {
            writeSaxAndStax(medicines, outPath);
        } catch (IOException | XMLStreamException e) {
            Logger.getLogger(DOMController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }
    }

    public static void writeSaxAndStax(List<Medicines.Medicine> Medicines, String path) throws IOException, XMLStreamException {
        final String[] medicinesArray = { Constants.NAME, Constants.PHARM, Constants.GROUP, Constants.ANALOGS, Constants.VERSION};
        final String[] versionArray = { Constants.CERTIFICATE, Constants.PACKAGE, Constants.DOSAGE };
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter;
        int depth = 0;
        try (FileWriter fileWriter = new FileWriter(path)) {
            xmlStreamWriter = outputFactory.createXMLStreamWriter(fileWriter);
            xmlStreamWriter.writeStartDocument();

            xmlStreamWriter.writeCharacters("\n");
            xmlStreamWriter.writeStartElement(Constants.MEDICINES);
            xmlStreamWriter.writeAttribute("xmlns", "http://www.nure.ua");
            xmlStreamWriter.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlStreamWriter.writeAttribute("xsi:schemaLocation", "http://www.nure.ua ../resources/input.xsd");
            depth++;
            for (Medicines.Medicine medicine : Medicines) {
                List<Object> objectsMedicine = DOMController.medicineToList(medicine);


                xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));

                xmlStreamWriter.writeStartElement(Constants.MEDICINE);

                depth++;
                for (int i = 0; i < 3; i++) {
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                    xmlStreamWriter.writeStartElement(medicinesArray[i]);
                    xmlStreamWriter.writeCharacters(objectsMedicine.get(i).toString());
                    xmlStreamWriter.writeEndElement();
                }

                xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                xmlStreamWriter.writeStartElement(Constants.ANALOGS);
                //xmlStreamWriter.w
                xmlStreamWriter.writeCharacters(String.join(",\n", medicine.getAnalogs()));
                //xmlStreamWriter.writeC
                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                xmlStreamWriter.writeStartElement(Constants.VERSIONS);
                depth++;
                xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                for (int i = 0; i < medicine.getVersions().getVersionList().size(); i++) {
                    xmlStreamWriter.writeStartElement(Constants.VERSION);
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(++depth * 4));
                    xmlStreamWriter.writeEmptyElement(versionArray[0]);
                    xmlStreamWriter.writeAttribute(Constants.NUMBER, medicine.getVersions().getVersionList().get(i).getCertificate().getNumber());
                    xmlStreamWriter.writeAttribute(Constants.DATA_FROM, medicine.getVersions().getVersionList().get(i).getCertificate().getDataFrom());
                    xmlStreamWriter.writeAttribute(Constants.DATA_TO, medicine.getVersions().getVersionList().get(i).getCertificate().getDataTo());

                    //xmlStreamWriter.writeStartElement(versionArray[1]);
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                    xmlStreamWriter.writeEmptyElement(versionArray[1]);
                    xmlStreamWriter.writeAttribute(Constants.TYPE, medicine.getVersions().getVersionList().get(i).getAPackage().getType());
                    xmlStreamWriter.writeAttribute(Constants.NUMBER_IN_PACKAGE, String.valueOf(medicine.getVersions().getVersionList().get(i).getAPackage().getNumberInPackage()));
                    xmlStreamWriter.writeAttribute(Constants.PRICE_FOR_PACKAGE, medicine.getVersions().getVersionList().get(i).getAPackage().getPriceForPackage());
                    //xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));
                    xmlStreamWriter.writeStartElement(versionArray[2]);
                    xmlStreamWriter.writeCharacters(medicine.getVersions().getVersionList().get(i).getDosage());
                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(--depth * 4));

                    xmlStreamWriter.writeEndElement();
                    xmlStreamWriter.writeCharacters("\n" + " ".repeat(depth * 4));

                }
                xmlStreamWriter.writeCharacters("\n" + " ".repeat(--depth * 4));

                xmlStreamWriter.writeEndElement();
                xmlStreamWriter.writeCharacters("\n" + " ".repeat(--depth * 4));

                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeCharacters("\n" + " ".repeat(--depth * 4));
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
        }
    }

}