/*
 * Copyright 2018 Andy Turner.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.data.stats19.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import uk.ac.leeds.ccg.data.Data_VariableType;
import uk.ac.leeds.ccg.data.Data_VariableType.Data_VariableNamesAndTypes;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Environment;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Object;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Strings;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;

/**
 * This class produces source code for loading STATS19 data. Source code classes
 * written in order to load the Accident data is written to
 * uk.ac.leeds.ccg.andyt.generic.data.stats19.data.accident. Source code classes
 * written in order to load the vehicle data is written to
 * uk.ac.leeds.ccg.andyt.generic.data.stats19.data.vehicle. Source code classes
 * written in order to load the casualty data is written to
 * uk.ac.leeds.ccg.andyt.generic.data.stats19.data.casualty.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_JavaCodeGenerator extends STATS19_Object {
    //Data_VariableType {

    private static final long serialVersionUID = 1L;

    public STATS19_Environment env;
    
    public STATS19_JavaCodeGenerator(STATS19_Environment e) {
        super(e);
        this.env = e;
    }

    public static void main(String[] args) {
        try {
            Path dataDir = Paths.get(System.getProperty("user.home"),
                    STATS19_Strings.s_data, STATS19_Strings.s_data,
                    STATS19_Strings.s_STATS19);
            STATS19_Environment e = new STATS19_Environment(
                    new Data_Environment(new Generic_Environment(
                    new Generic_Defaults(dataDir))));
            STATS19_JavaCodeGenerator p = new STATS19_JavaCodeGenerator(e);
            p.run();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    Data_VariableType vt;
    
    public void run() throws FileNotFoundException, IOException {
        vt = new Data_VariableType(env.de);
        vt.setDelimiter(",");
        String type;

        /**
         *
         */
        int dp = 5;

        Path indir = env.files.getInputDir();
        Path[] fs = new Path[2];
        fs[0] = Paths.get(indir.toString(), "Accidents_2018.csv");
        fs[1] = Paths.get(indir.toString(), "Accidents_2017.csv");
        //t = getFieldTypes(Integer.MAX_VALUE, fs, dp);
        Data_VariableNamesAndTypes vnt = vt.getFieldTypes(100, fs, dp, 1, "t");
        type = "accident";
        run(type, vnt);

//        type = "casualty";
//        f = getInputFile(type, files.getInputDataDir());
//        t = getFieldTypes(f, dp);
//        run(type, t);
//
//        type = "vehicle";
//        f = getInputFile(type, files.getInputDataDir());
//        t = getFieldTypes(f, dp);
//        run(type, t);
    }

    public void run(String type, Data_VariableNamesAndTypes vnt) throws IOException {
        Path outdir = Paths.get(System.getProperty("user.dir"), "src", "main"
                , "java", "uk", "ac", "leeds", "ccg", "data", "stats19", "data");
        outdir = Paths.get(outdir.toString(), type);
        Files.createDirectories(outdir);
        String packageName = "uk.ac.leeds.ccg.data.stats19.data." + type;
        String prepend = "STATS19";
        type = type.toUpperCase();
        String className = prepend + "_" + type + "_Record";
        Path fout = Paths.get(outdir.toString(), className + ".java");
        try (PrintWriter pw = Generic_IO.getPrintWriter(fout, false)) {
            writeHeaderPackageAndImports(pw, packageName, "", vnt);
            printClassDeclarationSerialVersionUID(pw, packageName, className, "", "");
            // Print Field Declarations Inits And Getters
            // initialise type2TypeName
            HashMap<Integer, String> type2TypeName = vt.getType2TypeName();
            //vnt.fieldNames2Order;
            // Print field declaration
            Iterator<Integer> ite = vnt.order2FieldNames.keySet().iterator();
            while (ite.hasNext()) {
                int index = ite.next();
                String field = vnt.order2FieldNames.get(index);
                int typ = vnt.order2Type.get(index);
                String typeName = type2TypeName.get(typ);
                // print field declaration
                pw.println("protected " + typeName + " " + field + ";");
            }
            // Print inits
            ite = vnt.order2FieldNames.keySet().iterator();
            while (ite.hasNext()) {
                int index = ite.next();
                String field = vnt.order2FieldNames.get(index);
                int typ = vnt.order2Type.get(index);
                String typeName = type2TypeName.get(typ);

//                printFieldDeclarationsInitsAndGetters(pw, fields, fieldTypes, v0);
//                // Constructor
//                pw.println("public " + className + "(String line) {");
//                pw.println("s = line.split(\"\\t\");");
//                for (int j = 0; j < headers[w].length; j++) {
//                    pw.println("init" + headers[w][j] + "(s[" + j + "]);");
//                }
            }
            pw.println("}");
            pw.println("}");
        }

    }

    /**
     *
     * @param pw
     * @param packageName
     * @param imports
     */
    public void writeHeaderPackageAndImports(PrintWriter pw,
            String packageName, String imports, Data_VariableNamesAndTypes vnt) {
        pw.println("/**");
        pw.println(" * Source code generated by " + this.getClass().getName());
        pw.println(" */");
        pw.println("package " + packageName + ";");
        if (!imports.isEmpty()) {
            pw.println("import " + imports + ";");
        }
        boolean hasBigDecimals = false;
        for (int i = 0; i < vnt.bigDecimals.length; i ++) {
            if (vnt.bigDecimals[i]) {
                hasBigDecimals = true;
                break;
            }
        }
        if (hasBigDecimals) {
            pw.println("import BigDecimal;");
        }
        boolean hasBigIntegers = false;
        for (int i = 0; i < vnt.bigIntegers.length; i ++) {
            if (vnt.bigIntegers[i]) {
                hasBigIntegers = true;
                break;
            }
        }
        if (hasBigIntegers) {
            pw.println("import BigInteger;");
        }
    }

    /**
     *
     * @param pw
     * @param packageName
     * @param className
     * @param implementations
     * @param extendedClassName
     */
    public void printClassDeclarationSerialVersionUID(PrintWriter pw,
            String packageName, String className, String implementations,
            String extendedClassName) {
        pw.print("public class " + className);
        if (!extendedClassName.isEmpty()) {
            pw.print(" extends " + extendedClassName);
        }
        if (!implementations.isEmpty()) {
            pw.print(" implements " + implementations);
        }
        pw.println(" {");
        /**
         * This is not included for performance reasons. pw.println("private
         * static final long serialVersionUID = " + serialVersionUID + ";");
         */
    }

    /**
     * @param pw
     * @param fields
     * @param fieldTypes
     * @param v0
     */
    public void printFieldDeclarationsInitsAndGetters(PrintWriter pw,
            TreeSet<String> fields, HashMap<String, Integer> fieldTypes,
            HashMap<String, Byte> v0) {
        // Field declarations
        printFieldDeclarations(pw, fields, fieldTypes);
        // Field init
        printFieldInits(pw, fields, fieldTypes, v0);
        // Field getters
        printFieldGetters(pw, fields, fieldTypes);
    }

    /**
     * @param pw
     * @param fields
     * @param fieldTypes
     */
    public void printFieldDeclarations(PrintWriter pw, TreeSet<String> fields,
            HashMap<String, Integer> fieldTypes) {
        String field;
        int fieldType;
        Iterator<String> ite;
        ite = fields.iterator();
        while (ite.hasNext()) {
            field = ite.next();
            fieldType = fieldTypes.get(field);
            switch (fieldType) {
                case 0:
                    pw.println("protected String " + field + ";");
                    break;
                case 1:
                    pw.println("protected double " + field + ";");
                    break;
                case 2:
                    pw.println("protected int " + field + ";");
                    break;
                case 3:
                    pw.println("protected short " + field + ";");
                    break;
                case 4:
                    pw.println("protected byte " + field + ";");
                    break;
                default:
                    pw.println("protected boolean " + field + ";");
                    break;
            }
        }
    }

    /**
     *
     * @param pw
     * @param fields
     * @param fieldTypes
     */
    public void printFieldGetters(PrintWriter pw, TreeSet<String> fields,
            HashMap<String, Integer> fieldTypes) {
        String field;
        int fieldType;
        Iterator<String> ite;
        ite = fields.iterator();
        while (ite.hasNext()) {
            field = ite.next();
            fieldType = fieldTypes.get(field);
            switch (fieldType) {
                case 0:
                    pw.println("public String get" + field + "() {");
                    break;
                case 1:
                    pw.println("public double get" + field + "() {");
                    break;
                case 2:
                    pw.println("public int get" + field + "() {");
                    break;
                case 3:
                    pw.println("public short get" + field + "() {");
                    break;
                case 4:
                    pw.println("public byte get" + field + "() {");
                    break;
                default:
                    pw.println("public boolean get" + field + "() {");
                    break;
            }
            pw.println("return " + field + ";");
            pw.println("}");
            pw.println();
        }
    }

    /**
     *
     * @param pw
     * @param fields
     * @param fieldTypes
     * @param v0
     */
    public void printFieldInits(PrintWriter pw, TreeSet<String> fields,
            HashMap<String, Integer> fieldTypes, HashMap<String, Byte> v0) {
        String field;
        int fieldType;
        Iterator<String> ite;
        ite = fields.iterator();
        while (ite.hasNext()) {
            field = ite.next();
            fieldType = fieldTypes.get(field);
            pw.println("protected final void init" + field + "(String s) {");
            pw.println("if (!s.trim().isEmpty()) {");
            switch (fieldType) {
                    case 0:
                    pw.println(field + " = s;");
                    break;
                case 1:
                    pw.println(field + " = Double.parseDouble(s);");
                    pw.println("} else {");
                    pw.println(field + " = Double.NaN;");
                    break;
                case 2:
                    pw.println(field + " = Integer.parseInt(s);");
                    pw.println("} else {");
                    pw.println(field + " = Integer.MIN_VALUE;");
                    break;
                case 3:
                    pw.println(field + " = Short.parseShort(s);");
                    pw.println("} else {");
                    pw.println(field + " = Short.MIN_VALUE;");
                    break;
                case 4:
                    pw.println(field + " = Byte.parseByte(s);");
                    pw.println("} else {");
                    pw.println(field + " = Byte.MIN_VALUE;");
                    break;
                default:
                    pw.println("byte b = Byte.parseByte(s);");
                    if (v0.get(field) == null) {
                        pw.println(field + " = false;");
                    } else {
                        pw.println("if (b == " + v0.get(field) + ") {");
                        pw.println(field + " = false;");
                        pw.println("} else {");
                        pw.println(field + " = true;");
                        pw.println("}");
                    }
                    break;
            }
            pw.println("}");
            pw.println("}");
            pw.println();
        }
    }

    protected HashMap<String, Byte> setCommonBooleanMaps(
            HashMap<String, Byte>[] v0ms,
            HashMap<String, Byte>[] v1ms,
            TreeSet<String>[] allFields,
            HashMap<String, Integer> fieldTypes) {
        HashMap<String, Byte> v0m;
        HashMap<String, Byte> v1m;
        Iterator<String> ites0;
        Iterator<String> ites1;
        String field0;
        String field1;
        TreeSet<String> fields;
        fields = allFields[5];
        HashMap<String, Byte> v0m1;
        HashMap<String, Byte> v1m1;
        v0m1 = new HashMap<>();
        v1m1 = new HashMap<>();
        ites0 = fields.iterator();
        byte v0;
        Byte v1;
        Byte v01;
        Byte v11;
        while (ites0.hasNext()) {
            field0 = ites0.next();
            if (fieldTypes.get(field0) == 5) {
                for (int w = 0; w < v0ms.length; w++) {
                    v0m = v0ms[w];
                    v1m = v1ms[w];
                    ites1 = v0m.keySet().iterator();
                    while (ites1.hasNext()) {
                        field1 = ites1.next();
                        if (field0.equalsIgnoreCase(field1)) {
                            v0 = v0m.get(field1);
                            if (v1m == null) {
                                v1 = Byte.MIN_VALUE;
                            } else {
                                //System.out.println("field1 " + field1);
                                //System.out.println("field1 " + field1);
                                v1 = v1m.get(field1);
                                if (v1 == null) {
                                    v1 = Byte.MIN_VALUE;
                                }
                            }
                            v01 = v0m1.get(field1);
                            v11 = v1m1.get(field1);
                            if (v01 == null) {
                                v0m1.put(field1, v0);
                            } else {
                                if (v01 != v0) {
                                    // Field better stored as a byte than boolean.
                                    fieldTypes.put(field1, 4);
                                }
                                if (v11 == null) {
                                    v1m1.put(field1, v1);
                                } else {
                                    if (v1 != v11.byteValue()) {
                                        // Field better stored as a byte than boolean.
                                        fieldTypes.put(field1, 4);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return v0m1;
    }

    /**
     * Finds and returns those fields that are in common and those fields .
     * result[0] are the fields in common with all.
     *
     * @param headers
     * @return
     */
    public ArrayList<String>[] getFieldsList(ArrayList<String> headers) {
        ArrayList<String>[] r;
        int size;
        size = headers.size();
        r = new ArrayList[size];
        Iterator<String> ite;
        ite = headers.iterator();
        int i;
        i = 0;
        while (ite.hasNext()) {
            r[i] = getFieldsList(ite.next());
            i++;
        }
        return r;
    }

    /**
     *
     * @param fields
     * @return
     */
    public TreeSet<String> getFields(String[] fields) {
        TreeSet<String> r;
        r = new TreeSet<>();
        r.addAll(Arrays.asList(fields));
        return r;
    }

    /**
     *
     * @param s
     * @return
     */
    public ArrayList<String> getFieldsList(String s) {
        ArrayList<String> r;
        r = new ArrayList<>();
        String[] split;
        split = s.split("\t");
        r.addAll(Arrays.asList(split));
        return r;
    }

    /**
     * Returns all the values common to s1, s2, s3, s4 and s5 and removes all
     * these common fields from s1, s2, s3, s4 and s5.
     *
     * @param s1
     * @param s2
     * @param s3 May be null.
     * @param s4 May be null.
     * @param s5 May be null.
     * @return
     * @Todo generalise
     */
    public TreeSet<String> getFieldsInCommon(TreeSet<String> s1,
            TreeSet<String> s2, TreeSet<String> s3, TreeSet<String> s4,
            TreeSet<String> s5) {
        TreeSet<String> result;
        result = new TreeSet<>();
        result.addAll(s1);
        result.retainAll(s2);
        if (s3 != null) {
            result.retainAll(s3);
        }
        if (s4 != null) {
            result.retainAll(s4);
        }
        if (s5 != null) {
            result.retainAll(s5);
        }
        return result;
    }
}
