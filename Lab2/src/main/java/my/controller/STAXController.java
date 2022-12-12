package my.controller;

import lombok.Getter;
import my.Constants;
import my.XML.Medicines;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class STAXController extends DefaultHandler {

    private final String filename;

    @Getter
    private final List<Medicines.Medicine> medicines;
    Medicines.Medicine medicine;

    public STAXController(String filename) {
        medicines = new ArrayList<>();
        this.filename = filename;
    }


    public void parse() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        try {
            XMLEventReader eventReader = factory.createXMLEventReader(new StreamSource(filename));
            while (eventReader.hasNext()) {
                XMLEvent xmlEvent = eventReader.nextEvent();
                readInput(xmlEvent,eventReader);
            }
        } catch (XMLStreamException e) {
            Logger.getLogger(STAXController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }
    }

    public void sortXML(){
        Collections.sort(medicines);
    }

    private void readInput(XMLEvent xmlEvent, XMLEventReader eventReader) throws XMLStreamException {

        if (xmlEvent.isStartElement()) {
            StartElement startElement = xmlEvent.asStartElement();
            if (startElement.getName().getLocalPart().equals(Constants.MEDICINE)) {
                medicine = new Medicines.Medicine();
            }
            else if (startElement.getName().getLocalPart().equals(Constants.NAME)) {
                xmlEvent = eventReader.nextEvent();
                medicine.setName(xmlEvent.asCharacters().getData());
            }
            else if (startElement.getName().getLocalPart().equals(Constants.PHARM)) {
                xmlEvent = eventReader.nextEvent();
                medicine.setPharm(xmlEvent.asCharacters().getData());
            }
            else if (startElement.getName().getLocalPart().equals(Constants.GROUP)) {
                xmlEvent = eventReader.nextEvent();
                medicine.setGroup(xmlEvent.asCharacters().getData());
            } else if (startElement.getName().getLocalPart().equals(Constants.ANALOGS)) {
                xmlEvent = eventReader.nextEvent();
                medicine.setAnalogs(Arrays.stream(xmlEvent.asCharacters().getData().split(Constants.SPLIT_REGEX)).toList());
            }
            else if (startElement.getName().getLocalPart().equals(Constants.VERSIONS)) {
                medicine.setVersions(parseVersions(eventReader));
            }
        } else if (xmlEvent.isEndElement()) {
            EndElement endElement = xmlEvent.asEndElement();
            if (endElement.getName().getLocalPart().equals(Constants.MEDICINE)) {
                medicines.add(medicine);
            }
        }
    }

    private Medicines.Medicine.Versions parseVersions(XMLEventReader reader) {
        XMLEvent event;
        StartElement element;
        Medicines.Medicine.Versions versions = new Medicines.Medicine.Versions(new ArrayList<>());
        Medicines.Medicine.Versions.Version version = new Medicines.Medicine.Versions.Version();
        while (true){
            try {

                event = reader.nextEvent();
                if (event.isStartElement()) {
                    element = event.asStartElement();
                    if (element.getName().getLocalPart().equals(Constants.CERTIFICATE)) {
                        //event.asCharacters().getData();
                        Medicines.Medicine.Versions.Version.Certificate tmpCertificate = new Medicines.Medicine.Versions.Version.Certificate();
                        tmpCertificate.setNumber(element.getAttributeByName(QName.valueOf(Constants.NUMBER)).getValue());
                        tmpCertificate.setDataFrom(element.getAttributeByName(QName.valueOf(Constants.DATA_FROM)).getValue());
                        tmpCertificate.setDataTo(element.getAttributeByName(QName.valueOf(Constants.DATA_TO)).getValue());
                        version.setCertificate(tmpCertificate);
                    } else if (element.getName().getLocalPart().equals(Constants.PACKAGE)) {
                        //event.asCharacters().getData();
                        Medicines.Medicine.Versions.Version.Package tmpPackage = new Medicines.Medicine.Versions.Version.Package();
                        tmpPackage.setType(element.getAttributeByName(QName.valueOf(Constants.TYPE)).getValue());
                        tmpPackage.setNumberInPackage(Integer.parseInt(element.getAttributeByName(QName.valueOf(Constants.NUMBER_IN_PACKAGE)).getValue()));
                        tmpPackage.setPriceForPackage(element.getAttributeByName(QName.valueOf(Constants.PRICE_FOR_PACKAGE)).getValue());
                        version.setAPackage(tmpPackage);
                    }
                    else if (element.getName().getLocalPart().equals(Constants.DOSAGE)) {
                        event = reader.nextEvent();
                        version.setDosage(event.asCharacters().getData());
                    }
                }else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(Constants.VERSION)) {
                        versions.getVersionList().add(version);
                        version = new Medicines.Medicine.Versions.Version();
                    }else if (endElement.getName().getLocalPart().equals(Constants.VERSIONS)) {
                        return versions;
                    }
                }
            } catch (XMLStreamException e) {
                Logger.getLogger(STAXController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
            }
        }
    }


    public void writeXML(String outPath) {
        try {
            SAXController.writeSaxAndStax(medicines, outPath);
        } catch (IOException | XMLStreamException e) {
            Logger.getLogger(STAXController.class.getName()).log(Level.SEVERE, Constants.ERROR, e);
        }
    }

}