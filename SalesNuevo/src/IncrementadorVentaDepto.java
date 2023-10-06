import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.Scanner;


public class IncrementadorVentaDepto {
    //para evitar confusiones propias decidi poner el Main al final
    private static double Porcentaje(Scanner sc) {
        double porcentaje;
        do {
            System.out.println("Incrementador de Precios");
            System.out.println(" ");
            System.out.println("NOTA: LOS PORCENTAJES FUERA DE ESE RANGO SON INVALIDO");
            System.out.println("");
            System.out.println("Ingrese el porcentaje a incrementar (entre 5% y 15%): ");
            porcentaje = sc.nextDouble();
        } while (porcentaje < 5 || porcentaje > 15);
        return porcentaje;}
    private static void LeeryUsarxml(double porc, String dpto) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document lectura = builder.parse(new File("sales.xml"));
        Element ElementoRiaz = lectura.getDocumentElement();
        NodeList saleRecords = ElementoRiaz.getElementsByTagName("sale_record");
        for (int i = 0; i < saleRecords.getLength(); i++) {
            Element saleRecord = (Element) saleRecords.item(i);
            Element departmentElement = (Element) saleRecord.getElementsByTagName("department").item(0);
            if (departmentElement.getTextContent().equals(dpto)) {
                Element Ventas = (Element) saleRecord.getElementsByTagName("sales").item(0);
                double VentaVieja = Double.parseDouble(Ventas.getTextContent());
                double VentaNueva = VentaVieja * (1 + porc / 100);
                //NOTA PARA MI EL 2F ME DARA MI RESULTADO CON DOS DECIMALES
                Ventas.setTextContent(String.format("%.2f", VentaNueva));}}
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource Principal = new DOMSource(lectura);
        StreamResult resultado = new StreamResult(new File("new_sales.xml"));
        transformer.transform(Principal, resultado);
    }
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            double porcentaje = Porcentaje(scanner);
            if (porcentaje < 5 || porcentaje > 15) {
                System.out.println("Porcentaje fuera del rango");
                return;
            } String NomDep =NombreDepartamento(scanner);
            LeeryUsarxml(porcentaje, NomDep);
            System.out.println("Archivo new_sales.xml creado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String NombreDepartamento(Scanner sc) {
        System.out.print("El departamento a actualizar: ");
        return sc.next(); }
}
