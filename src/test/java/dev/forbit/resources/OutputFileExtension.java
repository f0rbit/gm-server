package dev.forbit.resources;

import dev.forbit.server.utilities.Utilities;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Level;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class OutputFileExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            started = true;
            // Your "before all tests" startup logic goes here
            Utilities.addLogOutputFile(Level.ALL, "./build/test-output-all.log");
            Utilities.addLogOutputFile(Level.WARNING, "./build/test-output-warning.log");
            // The following line registers a callback hook when the root test context is shut down
            context.getRoot().getStore(GLOBAL).put("any unique name", this);
        }
    }

    @Override
    public void close() {
        // Your "after all tests" logic goes here
    }

}
