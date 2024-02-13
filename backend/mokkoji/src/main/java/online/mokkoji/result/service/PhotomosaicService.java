package online.mokkoji.result.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.ResultErrorCode;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PhotomosaicService {

    public double calculateDistance(Scalar color1, Scalar color2) {
        double distance = 0.0;
        for (int i = 0; i < color1.val.length; i++) {
            distance += Math.pow(color1.val[i] - color2.val[i], 2);
        }
        return Math.sqrt(distance);
    }

    public List<Mat> getComponentImages(String path, int size) throws IOException {
        List<Mat> images = new ArrayList<>();
        List<Scalar> avgColors = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), "*.{png,jpg,jpeg}")) {
            for (Path entry : stream) {
                Mat image = Imgcodecs.imread(entry.toString());
                Imgproc.resize(image, image, new Size(size, size));
                images.add(image);
                Scalar avgColor = Core.mean(image);
                avgColors.add(avgColor);
            }
        }

        return images;
    }

    public String createPhotomosaic(String thumbnailPath, String cellImagesPath) {
        Mat inputImage = Imgcodecs.imread(thumbnailPath);
        int height = inputImage.rows();
        int width = inputImage.cols();
        Mat blankImage = Mat.zeros(height, width, CvType.CV_8UC3);

        File cellImagesFolder = new File(cellImagesPath);
        File[] images = cellImagesFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png"));
        int totalImages = images.length;

        int stride = (int) Math.sqrt((height * width) / totalImages);

        for (int widthIdx = 0; widthIdx < width / stride; widthIdx++) {
            for (int heightIdx = 0; heightIdx < height / stride; heightIdx++) {
                Rect roi = new Rect(widthIdx * stride, heightIdx * stride, stride, stride);
                Mat partialInputImage = new Mat(inputImage, roi);
                Scalar partialAvgColor = Core.mean(partialInputImage);

                double minDistance = Double.MAX_VALUE;
                int imageIdx = -1;
                for (int idx = 0; idx < totalImages; idx++) {
                    Mat image = Imgcodecs.imread(images[idx].getAbsolutePath());
                    double distance = calculateDistance(partialAvgColor, Core.mean(image));
                    if (distance < minDistance) {
                        minDistance = distance;
                        imageIdx = idx;
                    }
                }

                Mat selectedImage = Imgcodecs.imread(images[imageIdx].getAbsolutePath());
                selectedImage.copyTo(blankImage.submat(roi));
            }
        }

        String outputFilePath = System.getProperty("user.home") + File.separator + "Desktop" +
                File.separator + "photomosaic.jpeg";
        Imgcodecs.imwrite(outputFilePath, blankImage);

        return outputFilePath;
    }

    public void deleteCellImages(String localPath) {

        File directory = new File(localPath);

        if(!directory.exists() || !directory.isDirectory())
            throw new RestApiException(ResultErrorCode.NONE_FOLDER_DIRECTORY);

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteCellImages(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}

