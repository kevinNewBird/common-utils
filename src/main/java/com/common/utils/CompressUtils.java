package com.common.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.function.Predicate;

/**
 * description: com.common.utils</BR>
 * TIP:
 * 只保留软连接文件，但会抹除软连接（需要单独处理，但可能达不到原来的软连接效果）
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/9/13
 * version: 1.0
 */
public final class CompressUtils {

    private CompressUtils() {
    }

    public static void main(String[] args) throws IOException {
        decompressAllByTar("D:\\work-test\\test.tar.gz", "D:\\work-test\\output", innerFile -> innerFile != null && innerFile.getName().toLowerCase().startsWith("python36") && innerFile.getName().toLowerCase().endsWith("tar.gz"));
    }


    public static void decompressAllByTar(String srcTarFile, String targetDir) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }
        String tmpSrcFile = srcTarFile.toLowerCase();
        if (StringUtils.endsWithAny(tmpSrcFile, ".tar.gz", "tgz")) {
            decompressByTGZ(srcTarFile, targetDir, null);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar.bz2")) {
            decompressByTarBZ(srcTarFile, targetDir, null);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar")) {
            decompressByTar(srcTarFile, targetDir, null);
        } else {
            throw new RuntimeException("Unsupported Compressed file type!");
        }
    }

    /**
     * 解压tar.gz、tgz、tar、tar.bz2
     * description:
     * create by: zhaosong 2024/9/13 10:26
     */
    public static void decompressAllByTar(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }
        String tmpSrcFile = srcTarFile.toLowerCase();
        if (StringUtils.endsWithAny(tmpSrcFile, ".tar.gz", "tgz")) {
            decompressByTGZ(srcTarFile, targetDir, condition);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar.bz2")) {
            decompressByTarBZ(srcTarFile, targetDir, condition);
        } else if (StringUtils.endsWithAny(tmpSrcFile, ".tar")) {
            decompressByTar(srcTarFile, targetDir, condition);
        } else {
            throw new RuntimeException("Unsupported Compressed file type!");
        }
    }


    /**
     * 解压tar.gz或tgz
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    public static void decompressByTGZ(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }

        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile)); InputStream gzip = new GzipCompressorInputStream(is); TarArchiveInputStream tis = new TarArchiveInputStream(gzip)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压tar.bz2
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    public static void decompressByTarBZ(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }

        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile)); InputStream bzip = new BZip2CompressorInputStream(is); TarArchiveInputStream tis = new TarArchiveInputStream(bzip)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压tar文件
     * description:
     * create by: zhaosong 2024/9/13 10:07
     *
     * @param srcTarFile
     * @param targetDir
     * @throws IOException
     */
    public static void decompressByTar(String srcTarFile, String targetDir, Predicate<File> condition) throws IOException {
        if (StringUtils.isBlank(srcTarFile) || !Files.exists(Paths.get(srcTarFile))) {
            throw new RuntimeException(MessageFormat.format("The compress file [{0}] is empty string or not exists!", srcTarFile));
        }

        try (InputStream is = Files.newInputStream(Paths.get(srcTarFile)); TarArchiveInputStream tis = new TarArchiveInputStream(is)) {
            doDecompress(tis, targetDir, condition);
        }
    }

    /**
     * 解压文件
     * description:
     * create by: zhaosong 2024/9/13 10:23
     *
     * @param tis
     * @param targetDir
     * @throws IOException
     */
    private static void doDecompress(TarArchiveInputStream tis, String targetDir, Predicate<File> condition) throws IOException {
        TarArchiveEntry entry = null;
        while ((entry = tis.getNextEntry()) != null) {
            if (!tis.canReadEntryData(entry)) {
                System.out.println("No Read Auth:" + entry.getName());
                continue;
            }
            File targetFile = new File(targetDir, entry.getName());
            if (entry.isDirectory()) {
                if (!targetFile.isDirectory() && !targetFile.mkdirs()) {
                    System.out.println("....");
                }
            } else {
                File parentFile = targetFile.getParentFile();
                if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                    System.out.println("....");
                }
                try (OutputStream os = Files.newOutputStream(targetFile.toPath())) {
                    IOUtils.copy(tis, os);
                }
                if (condition != null && condition.test(targetFile)) {
                    decompressAllByTar(targetFile.getAbsolutePath(), targetFile.getParent());
                }
            }
        }
    }

}
