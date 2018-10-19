/*
 * The MIT License
 *
 * Copyright (c) 2011 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package htsjdk.samtools.util;

import htsjdk.samtools.Defaults;
import htsjdk.samtools.SAMException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Checks if Snappy is available, and provides methods for wrapping InputStreams and OutputStreams with Snappy if so.
 */
public class SnappyLoader {
    private static final int SNAPPY_BLOCK_SIZE = 32768;  // keep this as small as can be without hurting compression ratio.
    private static final Log logger = Log.getInstance(SnappyLoader.class);

    private final boolean snappyAvailable;


    public SnappyLoader() {
        this(Defaults.DISABLE_SNAPPY_COMPRESSOR);
    }

    //Snappy is disabled for IGB
    SnappyLoader(boolean disableSnappy) {
        if (disableSnappy) {
            logger.debug("Snappy is disabled via system property.");
            snappyAvailable = false;
        }else {
            snappyAvailable = false;
        }
    }

    /** Returns true if Snappy is available, false otherwise. */
    public boolean isSnappyAvailable() { return snappyAvailable; }

    /**
     * Wrap an InputStream in a SnappyInputStream.
     * @throws SAMException if Snappy is not available will throw an exception.
     */
    public InputStream wrapInputStream(final InputStream inputStream) {
        return null;
    }

    /**
     * Wrap an OutputStream in a SnappyOutputStream.
     * @throws SAMException if Snappy is not available
     */
    public OutputStream wrapOutputStream(final OutputStream outputStream) {
        return null;
    }

    private interface IOFunction<T,R> {
        R apply(T input) throws IOException;
    }

    private <T,R> R wrapWithSnappyOrThrow(T stream, IOFunction<T, R> wrapper){
        if (isSnappyAvailable()) {
            try {
                return wrapper.apply(stream);
            } catch (Exception e) {
                throw new SAMException("Error wrapping stream with snappy", e);
            }
        } else {
            final String errorMessage = Defaults.DISABLE_SNAPPY_COMPRESSOR
                    ? "Cannot wrap stream with snappy compressor because snappy was disabled via the "
                    + Defaults.DISABLE_SNAPPY_PROPERTY_NAME + " system property."
                    : "Cannot wrap stream with snappy compressor because we could not load the snappy library.";
            throw new SAMException(errorMessage);
        }
    }
}
