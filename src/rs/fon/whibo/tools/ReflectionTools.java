package rs.fon.whibo.tools;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionTools {

	public static String[] getComponentClassNamesForSubproblem(
			Class subproblemBaseClass) {

		boolean withinJarFile = subproblemBaseClass.getProtectionDomain()
				.getCodeSource().getLocation().getPath().endsWith(".jar");

		if (withinJarFile)
			return getSubClassesFromJarFile(subproblemBaseClass);
		else
			return getSubClassesFromFolder(subproblemBaseClass);
	}

	private static String[] getSubClassesFromJarFile(Class baseClass) {
		ArrayList<String> classNames = new ArrayList<String>();

		try {
			String jarFileAbsolutePath = baseClass.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			String packageName = baseClass.getPackage().getName();
			String packagePath = packageName.replace('.', '/');

			jarFileAbsolutePath = jarFileAbsolutePath.replace("%20", " ");
			URL url = new URL("jar:file:" + jarFileAbsolutePath + "!/");

			JarURLConnection conn = (JarURLConnection) url.openConnection();
			JarFile jfile = conn.getJarFile();
			Enumeration<JarEntry> e = jfile.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				String entryname = entry.getName();
				if (entryname.matches(packagePath + ".*\\.class")) {
					String classname = entryname.substring(0,
							entryname.length() - 6);
					if (classname.startsWith("/"))
						classname = classname.substring(1);
					classname = classname.replace('/', '.');
					try {
						Class c = Class.forName(classname);
						if (baseClass.isAssignableFrom(c)
								&& !Modifier.isAbstract(c.getModifiers())
								&& !c.equals(baseClass)) {
							classNames.add(classname);
						}
					} catch (ClassNotFoundException cnfex) {
						System.err.println(cnfex.toString());
					}
				}
			}

			return classNames.toArray(new String[] {});

		} catch (Exception e) {
			System.err.println(e.toString());
			return new String[] {};
		}
	}

	private static String[] getSubClassesFromFolder(Class baseClass) {
		ArrayList<String> classNames = new ArrayList<String>();
		try {

			String absolutePath = baseClass.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			String packageName = baseClass.getPackage().getName();

			absolutePath = absolutePath.replace("%20", " ");
			if (!absolutePath.startsWith("/")) {
				absolutePath = "/" + absolutePath;
			}
			String packagePath = packageName.replace('.', '/');

			URL url = new URL("file:" + absolutePath + packagePath);

			File directory = new File(url.getFile());
			directory = new File(directory.getAbsolutePath()
					.replace("%20", " "));

			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {

				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					String classname = files[i].substring(0,
							files[i].length() - 6);
					try {
						Class c = Class.forName(packageName + "." + classname);
						if (baseClass.isAssignableFrom(c)
								&& !Modifier.isAbstract(c.getModifiers())
								&& !c.equals(baseClass)) {
							classNames.add(packageName + "." + classname);
						}
					} catch (ClassNotFoundException cnfex) {
						System.err.println(cnfex);
					}
				}
			}
			return classNames.toArray(new String[] {});

		} catch (Exception e) {
			System.err.println(e);
			return new String[] {};
		}
	}

}
