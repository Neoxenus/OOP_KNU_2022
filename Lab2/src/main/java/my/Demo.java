package my;

import my.controller.DOMController;
import my.controller.SAXController;
import my.controller.STAXController;

public class Demo {

    public static void main(String[] args) {
        System.out.println("Input ==> " + Constants.INPUT_XML);

        ////////////////////////////////////////////////////////
        // DOM
        ////////////////////////////////////////////////////////

        // get container
        DOMController domController = new DOMController(Constants.INPUT_XML);
        domController.parseXML(true);
        domController.sortXML();
        domController.saveToXML(Constants.DOM_RESULT);
        System.out.println("DOM Output ==> " + Constants.DOM_RESULT);
        ////////////////////////////////////////////////////////
        // SAX
        ////////////////////////////////////////////////////////
        SAXController saxController = new SAXController(Constants.INPUT_XML);
        saxController.parse(true);
        saxController.sortXML();
        saxController.writeXML(Constants.SAX_RESULT);
        System.out.println("SAX Output ==> " + Constants.SAX_RESULT);

        ////////////////////////////////////////////////////////
        // StAX
        ////////////////////////////////////////////////////////
        STAXController staxController = new STAXController(Constants.INPUT_XML);
        staxController.parse();
        staxController.sortXML();
        staxController.writeXML(Constants.STAX_RESULT);

        System.out.println("STAX Output ==> " + Constants.STAX_RESULT);

    }

}