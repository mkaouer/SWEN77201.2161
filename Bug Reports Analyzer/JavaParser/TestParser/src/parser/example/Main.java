package parser.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Main {
    private static PrintWriter printWriter;
    private static StringBuilder stringBuilder;
    private static Map<String, ArrayList<String>> classes;
    private static ArrayList<String> methods;

    public static void main(String[] args) throws IOException {
        stringBuilder = new StringBuilder();

        // This is where the CSV file will be created.
		printWriter = new PrintWriter(new File("/Users/ibrahim/Desktop/test/SignalResults.csv"));

        // This folder is where the program will be analyzing
        //File folderToAnalyze = new File("/Users/Jan//Desktop/SWEN-772/Project/example");
		File folderToAnalyze = new File("/Users/ibrahim/Desktop/test/signal");

        // CSV file headers
        stringBuilder.append("File,Package,Class,Method\n");

        // Let's analyze the files!
        listFilesForFolder(folderToAnalyze);

        // Moves the data to CSV
        printWriter.write(stringBuilder.toString());

        // We're done with printWriter!
        printWriter.close();
    }

    public static void listFilesForFolder(File folder) throws IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.getName().endsWith(".java")) {
                    System.out.println("=============================");
                    System.out.println("File Name: " + fileEntry.getName());
                    System.out.println("=============================");
                    String packagename = fileEntry.getCanonicalPath();
                    String pk = packagename.replaceAll("/", ".");
                    System.out.println("Package Name: " + pk.substring(33));

                    parseClassesForFile(fileEntry);

                    for (Map.Entry<String, ArrayList<String>> entry : classes.entrySet()) {
                        String className = entry.getKey();
                        ArrayList<String> classMethods = entry.getValue();

                        if (classMethods.isEmpty()) {
                            // write the file name
                            stringBuilder.append(fileEntry.getName());
                            stringBuilder.append(",");

                            // write the pkg name
                            stringBuilder.append(pk.substring(33));
                            stringBuilder.append(",");

                            stringBuilder.append(className);
                            stringBuilder.append("\n");
                        }

                        for (String methodName : classMethods) {
                            // write the file name
                            stringBuilder.append(fileEntry.getName());
                            stringBuilder.append(",");

                            // write the pkg name
                            stringBuilder.append(pk.substring(33));
                            stringBuilder.append(",");

                            stringBuilder.append(className);
                            stringBuilder.append(",");

                            stringBuilder.append(methodName);
                            stringBuilder.append(",");
                            stringBuilder.append("\n");
                        }
                    }

                    stringBuilder.append("\n");
                }
            }
        }
    }

    public static void parseClassesForFile(File file) throws IOException {
        InputStream in = null;
        CompilationUnit cu;
        classes = new HashMap<String, ArrayList<String>>();

        try {
            in = new FileInputStream(file);
            cu = JavaParser.parse(in);

            new ClassVisitorAdapter().visit(cu, null);
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        } finally {
            in.close();
        }
    }
    
    // To get the classes
    private static class ClassVisitorAdapter extends VoidVisitorAdapter {
        @Override
        public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arg) {
            methods = new ArrayList<String>();

            System.out.println("Class Name: " + classOrInterfaceDeclaration.getName());

            if (!classOrInterfaceDeclaration.getMethods().isEmpty()) {
                for (MethodDeclaration method : classOrInterfaceDeclaration.getMethods()) {
                    System.out.println("Method Name: " + method.getName());
                    methods.add(method.getName());
                }
            }

            if (!classOrInterfaceDeclaration.isInterface()){
                classes.put(classOrInterfaceDeclaration.getName(), methods);
            }else{
                classes.put(classOrInterfaceDeclaration.getName() +" (Interface Class)", methods);
            }
        }
    }
}