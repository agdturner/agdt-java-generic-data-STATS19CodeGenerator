/*
 * Copyright 2018 Andy Turner, CCG, University of Leeds.
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
package uk.ac.leeds.ccg.data.stats19.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.data.stats19.data.STATS19_Data;
import uk.ac.leeds.ccg.data.stats19.io.STATS19_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.generic.memory.Generic_MemoryManager;

/**
 * STATS19_Environment
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_Environment extends Generic_MemoryManager {

    private static final long serialVersionUID = 1L;

    public transient final Data_Environment de;
    public transient final Generic_Environment env;
    
    public int logID;
    //public final STATS19_Casualty_Handler ch;
    public STATS19_Data data;
    public STATS19_Files files;
    
    public transient static final String EOL = System.getProperty("line.separator");
    
    public STATS19_Environment(Data_Environment de) throws IOException, Exception {
        //Memory_Threshold = 3000000000L;
        this.de = de;
        this.env = de.env;
        Path dir = Paths.get(de.files.getDataDir().toString());
        files = new STATS19_Files(dir);
//        Path f  = files.getEnvDataFile();
//        if (f.exists()) {
//            loadData();
//        } else {
//            data = new STATS19_Data(this);
//        }
        logID = env.initLog(STATS19_Strings.s_STATS19);
        //ch = new STATS19_Casualty_Handler(this, files.getInputDir());
    }

    /**
     * A method to try to ensure there is enough memory to continue.
     *
     * @return
     */
    @Override
    public boolean checkAndMaybeFreeMemory() throws IOException {
        System.gc();
        while (getTotalFreeMemory() < Memory_Threshold) {
//            int clear = clearAllData();
//            if (clear == 0) {
//                return false;
//            }
            if (!swapSomeData()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param hoome handleOutOfMemoryError
     * @return 
     */
    @Override
    public boolean swapSomeData(boolean hoome) throws IOException {
        try {
            boolean r = swapSomeData();
            checkAndMaybeFreeMemory();
            return r;
        } catch (OutOfMemoryError e) {
            if (hoome) {
                clearMemoryReserve(env);
                boolean r = swapSomeData(HOOMEF);
                initMemoryReserve(env);
                return r;
            } else {
                throw e;
            }
        }
    }

    /**
     * Currently this just tries to cache WaAS data.
     *
     * @return
     */
    @Override
    public boolean swapSomeData() throws IOException {
        boolean r = clearSomeData();
        if (r) {
            return r;
        } else {
            System.out.println("No WaAS data to clear. Do some coding to try "
                    + "to arrange to clear something else if needs be. If the "
                    + "program fails then try providing more memory...");
            return r;
        }
    }

    public boolean clearSomeData() throws IOException {
        return data.clearSomeData();
    }

    public int clearAllData() throws IOException {
        return data.clearAllData();
    }
    
    public void swapData() throws IOException {
        Path f = files.getEnvDataFile();
        String m = "Swap data to " + f;
        logStartTag(m);
        Generic_IO.writeObject(data, f);
        logEndTag(m);
    }

    public final void loadData() throws IOException, ClassNotFoundException {
        Path f = files.getEnvDataFile();
        String m = "loadData from " + f;
        logStartTag(m);
        data = (STATS19_Data) Generic_IO.readObject(f);
        logEndTag(m);
    }

    /**
     * For convenience.
     * {@link Generic_Environment#logStartTag(java.lang.String, int)}
     *
     * @param s The tag name.
     */
    public final void logStartTag(String s) {
        env.logStartTag(s, logID);
    }

    /**
     * For convenience. {@link Generic_Environment#log(java.lang.String, int)}
     *
     * @param s The message to be logged.
     */
    public void log(String s) {
        env.log(s, logID);
    }

    /**
     * For convenience.
     * {@link Generic_Environment#logEndTag(java.lang.String, int)}
     *
     * @param s The tag name.
     */
    public final void logEndTag(String s) {
        env.logEndTag(s, logID);
    }
}
