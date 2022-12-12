import java.util.Arrays;
import java.util.Scanner;

public class ClassInfoGetter {

    private void getInfoAboutClass() {
        Class<?> clazz = null;
        boolean classNotFound = true;

        while(classNotFound) {
            System.out.println("Enter class name: ");
            String className = new Scanner(System.in).next();
            classNotFound = false;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                System.out.println("No class with such name found");
                classNotFound = true;
            }
        }

        System.out.println("Class' name: " + clazz.getName());
        System.out.println("Class' simple name: " + clazz.getSimpleName());
        System.out.println("This class is primitive: " + clazz.isPrimitive());
        System.out.println("This class is interface: " + clazz.isInterface());
        System.out.println("This class is enum: " + clazz.isEnum());
        if(clazz.isEnum()) {
            System.out.println("Enum's constants: " + Arrays.toString(clazz.getEnumConstants()));
        }
        System.out.println("This class is annotation: " + clazz.isAnnotation());
        System.out.println("This class is member class: " + clazz.isMemberClass());
        System.out.println("This class is local class: " + clazz.isLocalClass());
        System.out.println("This class is anonymous: " + clazz.isAnonymousClass());
        if(clazz.isLocalClass() || clazz.isAnonymousClass() || clazz.isMemberClass()) {
            System.out.println("The enclosing class: " + clazz.getEnclosingClass());
            System.out.println("The enclosing constructor: " + clazz.getEnclosingConstructor());
            System.out.println("The enclosing method: " + clazz.getEnclosingMethod());
        }
        System.out.println("This class is array : " + clazz.isArray());
        System.out.println("This class is synthetic: " + clazz.isSynthetic());
        System.out.println("Class' declared constructors: " + Arrays.toString(clazz.getDeclaredConstructors()));
        System.out.println("Class' constructors: " + Arrays.toString(clazz.getConstructors()));
        System.out.println("Class' declared fields: " + Arrays.toString(clazz.getDeclaredFields()));
        System.out.println("Class' fields: " + Arrays.toString(clazz.getFields()));
        System.out.println("Class' declared methods: " + Arrays.toString(clazz.getDeclaredMethods()));
        System.out.println("Class' methods: " + Arrays.toString(clazz.getMethods()));
        System.out.println("Class' declared classes: " + Arrays.toString(clazz.getDeclaredClasses()));
        System.out.println("Class' declared annotations: " + Arrays.toString(clazz.getDeclaredAnnotations()));
        System.out.println("Class's superclass: " + clazz.getSuperclass());
        System.out.println("Class's package: " + clazz.getPackage());
        System.out.println("Class' interfaces: " + Arrays.toString(clazz.getInterfaces()));
    }

    public static void main(String[] args) {
        new ClassInfoGetter().getInfoAboutClass();
    }
}
