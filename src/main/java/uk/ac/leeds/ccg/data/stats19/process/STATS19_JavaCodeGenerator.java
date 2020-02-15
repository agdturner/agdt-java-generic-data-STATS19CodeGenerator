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
import java.util.HashMap;
import uk.ac.leeds.ccg.cg.process.CG_Process;
import uk.ac.leeds.ccg.data.Data_VariableType;
import uk.ac.leeds.ccg.data.Data_VariableType.Data_VariableNamesAndTypes;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Environment;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Strings;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.lang.Generic_String;

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
public class STATS19_JavaCodeGenerator extends CG_Process {

    public final STATS19_Environment se;
    public final Data_Environment de;
    public final Generic_Environment env;

    public STATS19_JavaCodeGenerator(STATS19_Environment e) {
        se = e;
        de = se.de;
        env = se.env;
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
        vt = new Data_VariableType(de);
        vt.setDelimiter(",");
        int dp = 5;
        Path indir = env.files.getInputDir();
        int startYear = 2009;
        int endYear = 2018;
        int nYears = endYear - startYear + 1;
        /**
         * Accident data
         */
        Path[] afs = new Path[nYears];
        for (int year = startYear; year <= endYear; year++) {
            afs[year - startYear] = Paths.get(indir.toString(), "Accidents_"
                    + Integer.toString(year) + ".csv");
        }
        Data_VariableNamesAndTypes avnt = vt.getVariableNamesAndTypes(100, afs,
                dp, 1, "t");
        run("accident", avnt);

        /**
         * Casualty data
         */
        Path[] cfs = new Path[nYears];
        for (int year = startYear; year <= endYear; year++) {
            cfs[year - startYear] = Paths.get(indir.toString(), "Casualties_"
                    + Integer.toString(year) + ".csv");
        }
        Data_VariableNamesAndTypes cvnt = vt.getVariableNamesAndTypes(100, cfs,
                dp, 1, "t");
        run("casualty", cvnt);

        /**
         * Vehicle data
         */
        Path[] vfs = new Path[nYears];
        for (int year = startYear; year <= endYear; year++) {
            vfs[year - startYear] = Paths.get(indir.toString(), "Vehicles_"
                    + Integer.toString(year) + ".csv");
        }
        Data_VariableNamesAndTypes vvnt = vt.getVariableNamesAndTypes(100, vfs,
                dp, 1, "t");
        run("vehicle", vvnt);
    }

    public void run(String type, Data_VariableNamesAndTypes vnt) throws IOException {
        Path outdir = Paths.get(System.getProperty("user.dir"), "src", "main",
                 "java", "uk", "ac", "leeds", "ccg", "data", "stats19", "data");
        outdir = Paths.get(outdir.toString(), type);
        Files.createDirectories(outdir);
        String packageName = "uk.ac.leeds.ccg.data.stats19.data." + type;
        String prepend = "STATS19";
        String className = prepend + "_" + Generic_String.getCapitalFirstLetter(
                type) + "_Record";
        Path fout = Paths.get(outdir.toString(), className + ".java");
        ArrayList<String> imports = new ArrayList<>();
        imports.add("uk.ac.leeds.ccg.data.Data_Record");
        imports.add("uk.ac.leeds.ccg.data.stats19.data.id.STATS19_RecordID");
        try (PrintWriter pw = Generic_IO.getPrintWriter(fout, false)) {
            writeHeaderPackageAndImports(pw, packageName, imports, vnt);
            printClassDeclaration(pw, packageName, className, "", "Data_Record");
            printSerialVersionUID(pw, 1);
            /**
             * Print Field Declarations Inits And Getters
             * ------------------------------------------
             */
//            /**
//             * Deal with the first field which for some reason has an extra
//             * characters at at the start of the field name. Deal with it be 
//             * removing this character
//             */
//            if (true) {
//                String field = vnt.order2FieldNames.get(0);
//                String newName = field.substring(0,1) + field.substring(2);
//                System.out.println(newName);
//                vnt.order2FieldNames.put(0, newName);
//                vnt.fieldNames2Order.put(newName, 0);
//            }
            HashMap<Integer, String> type2TypeName = vt.getType2TypeName();
            printFieldDeclarations(pw, type2TypeName, vnt);
            printConstructor(pw, className, prepend, vnt);
            printFieldInits(pw, vnt);
            printFieldGetters(pw, type2TypeName, vnt);
            pw.println("}");
        }

    }
}
