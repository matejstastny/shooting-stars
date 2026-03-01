/*
 * Author: Matej Stastny
 * Date created: 5/5/2024
 * Github link:  https://github.com/matejstastny/shooting-stars
 */

package shootingstars.common;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * AdvancedVariable - A versatile utility class for managing variables and
 * persisting them to JSON files. This class supports storing variables of any
 * type and saving them to JSON files for later retrieval using the Jackson
 * library for serialization and deserialization.
 *
 * <p>
 * Example Usage:
 * </p>
 *
 *
 * <pre>
 * // Creating an instance of AdvancedVariable to store a CustomObject
 * AdvancedVariable<CustomObject> customObjectVariable = new AdvancedVariable<>("customObjectFile.json");
 * CustomObject customObject = new CustomObject("John Doe", 30);
 * customObjectVariable.set(customObject);
 *
 * try {
 *     // Save the variable to JSON file
 *     customObjectVariable.save();
 *
 *     // Load the variable from JSON file
 *     customObjectVariable.loadFromFile(CustomObject.class);
 *     System.out.println("Loaded value: " + customObjectVariable.get().getName() + ", " + customObjectVariable.get().getAge());
 * } catch (IOException e) {
 *     e.printStackTrace();
 * }
 * </pre>
 *
 *
 *
 * @param <T> The type of the variable to be stored and managed.
 */
public class AdvancedVariable<T> {

    private T storedValue;
    private File saveFile;
    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    // Constructor ---------------------------------------------------------------

    /**
     * Default empty constructor. Initializes the default {@code ObjectMapper} and a
     * custom {@code ObjectWritter} with pretty writing (custom class shown below in
     * this file).
     */
    private AdvancedVariable() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectWriter = this.objectMapper.writer(new DefaultPrettyPrinter() {
            @Override
            public DefaultPrettyPrinter createInstance() {
                return new CustomPrettyPrinter();
            }
        });
    }

    /**
     * Initializes the save file and loads the value of the class type given from
     * the file.
     *
     * @param saveFile - save file.
     * @param type     - class type of the variable.
     * @throws IOException
     */
    public AdvancedVariable(File saveFile, Class<T> type) throws IOException {
        this(saveFile);
        loadFromFile(type);
    }

    /**
     * Initializes the save file and loads the value of the class type given from
     * the file.
     *
     * @param saveFilePath - save file path.
     * @param type         - class type of the variable.
     * @throws IOException
     */
    public AdvancedVariable(String saveFilePath, Class<T> type) throws IOException {
        this(new File(saveFilePath), type);
    }

    /**
     * Initializes the save file, doesn't set the value.
     *
     * @param saveFile - save file.
     */
    public AdvancedVariable(File saveFile) {
        this();
        this.saveFile = saveFile;
    }

    /**
     * Initializes the save file, doesn't set the value.
     *
     * @param saveFilePath - save file path.
     */
    public AdvancedVariable(String saveFilePath) {
        this(new File(saveFilePath));
    }

    /**
     * Initializes the save file, and sets the variable value.
     *
     * @param saveFile - save file.
     * @param value    - value that this variable was set to.
     */
    public AdvancedVariable(File saveFile, T value) {
        this();
        this.saveFile = saveFile;
        this.set(value);
    }

    /**
     * Initializes the save file, and sets the default value.
     *
     * @param saveFilePath - save file path.
     * @param value        - value that this variable was set to.
     */
    public AdvancedVariable(String saveFilePath, T value) {
        this(new File(saveFilePath), value);
    }

    // Getters -------------------------------------------------------------------

    public T get() {
        return storedValue;
    }

    // Modifiers -----------------------------------------------------------------

    public void set(T value) {
        this.storedValue = value;
    }

    public void setAndSave(T value) throws IOException {
        this.set(value);
        this.save();
    }

    public void save() throws IOException {
        this.objectWriter.writeValue(this.saveFile, storedValue);
    }

    /**
     * Loads a value from this variables save file, and than saves it into this
     * variable. Returns if the read was successful.
     *
     * @param valueType - {@code Class} of the variable this object is reading.
     * @return if the read was successful.
     * @throws IOException
     */
    public boolean loadFromFile(Class<T> valueType) throws IOException {
        if (this.saveFile.exists()) {
            storedValue = this.objectMapper.readValue(this.saveFile, valueType);
        } else {
            return false;
        }
        return true;
    }

    // PrettyPrinter -------------------------------------------------------------

    private static class CustomPrettyPrinter extends DefaultPrettyPrinter {
        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
            jg.writeRaw(": ");
        }

        @Override
        public void writeStartObject(JsonGenerator jg) throws IOException {
            jg.writeRaw("{\n");
        }

        @Override
        public void writeEndObject(JsonGenerator jg, int nrOfEntries) throws IOException {
            jg.writeRaw("\n");
            super.writeEndObject(jg, nrOfEntries);
        }

        @Override
        public void writeStartArray(JsonGenerator jg) throws IOException {
            jg.writeRaw("[\n");
        }

        @Override
        public void writeEndArray(JsonGenerator jg, int nrOfValues) throws IOException {
            jg.writeRaw("\n");
            super.writeEndArray(jg, nrOfValues);
        }
    }

}
