package cn.leomc.pvzmultiplayer;

import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Modified from https://github.com/crykn/guacamole/blob/master/gdx-desktop/src/main/java/de/damios/guacamole/gdx/StartOnFirstThreadHelper.java
public class StartOnFirstThreadHelper {

    private static final String JVM_RESTARTED_ARG = "jvmIsRestarted";

    public static boolean startNewJvmIfRequired(boolean redirectOutput) {
        if (!UIUtils.isMac)
            return false;

        if (!System.getProperty("org.graalvm.nativeimage.imagecode", "").isEmpty())
            return false;

        long pid = ProcessHandle.current().pid();
        if ("1".equals(System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid)))
            return false;


        if ("true".equals(System.getProperty(JVM_RESTARTED_ARG))) {
            System.err.println("The JVM was restarted, but the -XStartOnFirstThread argument could not be set.");
            return false;
        }


        List<String> command = new ArrayList<>();
        String separator = System.getProperty("file.separator");
        String javaExecPath = ProcessHandle.current().info().command().orElseGet(() ->
                System.getProperty("java.home") + separator + "bin" + separator + "java");
        if (!Files.exists(Path.of(javaExecPath))) {
            System.err.println("A Java installation could not be found.");
            return false;
        }
        command.add(javaExecPath);
        command.add("-XStartOnFirstThread");
        command.add("-D" + JVM_RESTARTED_ARG + "=true");
        command.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        if (mainClass == null) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            if (trace.length > 0) {
                mainClass = trace[trace.length - 1].getClassName();
            } else {
                System.err.println("The main class could not be determined.");
                return false;
            }
        }
        command.add(mainClass);

        try {
            if (!redirectOutput) {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.start();
            } else {
                System.out.println(command);
                Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
                BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = processOutput.readLine()) != null) {
                    System.out.println(line);
                }

                process.waitFor();
            }
        } catch (Exception e) {
            System.err.println("There was a problem restarting the JVM");
            e.printStackTrace();
        }

        return true;
    }

    public static boolean startNewJvmIfRequired() {
        return startNewJvmIfRequired(true);
    }
}