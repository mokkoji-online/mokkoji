package online.mokkoji.result.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.mokkoji.common.exception.RestApiException;
import online.mokkoji.common.exception.errorcode.ResultErrorCode;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
public class PhotomosaicService {

    private final String LOCAL_PATH = "/opt/result" + File.separator;

    private double calculateDistance(Scalar color1, Scalar color2) {
        double distance = 0.0;
        for (int i = 0; i < color1.val.length; i++) {
            distance += Math.pow(color1.val[i] - color2.val[i], 2);
        }
        return Math.sqrt(distance);
    }

    private List<Mat> getComponentImages(String path, int size) throws IOException {
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

    public String createPhotomosaic(Long resultId) {
        log.info("포토 모자이크 생성 시작");
        Mat inputImage = Imgcodecs.imread(LOCAL_PATH + resultId + File.separator + "thumbnail.jpg");
        int height = inputImage.rows();
        int width = inputImage.cols();
        Mat blankImage = Mat.zeros(height, width, CvType.CV_8UC3);

        List<Mat> images = null;
        try {
            images = getComponentImages(LOCAL_PATH + resultId + File.separator + "cellImages", 30);
            log.info("셀 이미지 개수 : {}", images.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < width / 30; i++) {
            for (int j = 0; j < height / 30; j++) {
                Rect roi = new Rect(i * 30, j * 30, 30, 30);
                Mat partialInputImage = new Mat(inputImage, roi);
                Scalar partialAvgColor = Core.mean(partialInputImage);

                double minDistance = Double.MAX_VALUE;
                int idx = -1;
                for (int k = 0; k < images.size(); k++) {
                    double distance = calculateDistance(partialAvgColor, Core.mean(images.get(k)));
                    if (distance < minDistance) {
                        minDistance = distance;
                        idx = k;
                    }
                }

                Mat selectedImage = images.get(idx);
                selectedImage.copyTo(blankImage.submat(roi));
            }
        }

        String outputImagePath = LOCAL_PATH + resultId + File.separator + "photomosaic.jpg";
        Imgcodecs.imwrite(outputImagePath, blankImage);

        log.info("생성 완료!!");
        return outputImagePath;
    }

    public void deleteImages(String imagesPath) {

        File directory = new File(imagesPath);

        if(!directory.exists() || !directory.isDirectory())
            throw new RestApiException(ResultErrorCode.NONE_FOLDER_DIRECTORY);

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteImages(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}

