/*
 * Copyright 2018 geoagdt.
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
package uk.ac.leeds.ccg.data.stats19.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.io.Data_Files;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Strings;

/**
 * STATS19_Files
 * 
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_Files extends Data_Files {

    private static final long serialVersionUID = 1L;

    /**
     * @param dir What {@link #dir} is set using.
     * @throws java.io.IOException If encountered.
     */
    public STATS19_Files(Path dir) throws IOException {
        super(dir);
    }

    public Path getSTATS19InputDir() throws IOException {
        return Paths.get(getInputDir().toString(), STATS19_Strings.s_STATS19);
    }

    public Path getGeneratedSTATS19Dir() throws IOException {
        Path r = Paths.get(getGeneratedDir().toString(), STATS19_Strings.s_STATS19);
        Files.createDirectories(r);
        return r;
    }
    
    public Path getGeneratedSTATS19SubsetsDir() throws IOException {
        Path r = Paths.get(getGeneratedSTATS19Dir().toString(), STATS19_Strings.s_Subsets);
        Files.createDirectories(r);
        return r;
    }
}
