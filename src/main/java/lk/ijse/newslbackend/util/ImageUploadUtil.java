package lk.ijse.newslbackend.util;

import lk.ijse.newslbackend.dto.UserResponseDTO;
import lk.ijse.newslbackend.exception.FileConversionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Base64;
import java.util.Objects;

public class ImageUploadUtil {
    private static final String BASE_PATH = System.getProperty("user.home") + "/IdeaProjects/NewSL-Backend/src/main/resources/assets/";
    private static final String IMAGES_PATH = BASE_PATH + "images/";
    private static final String PROFILE_IMAGES_PATH = IMAGES_PATH + "profile-images/";
    private static final String COVER_IMAGES_PATH = IMAGES_PATH + "cover-images/";
    private static final String[] ALLOWED_FORMATS = {".jpg", ".png", ".jpeg"};

    /**
     * Saves the uploaded image file in the user's directory. If an image with the same name already exists,
     * it will be replaced. The file name will follow the pattern `userid_type_photo.extension`.
     *
     * @param name        The ID of the user.
     * @param type          The type of image (e.g., "profile" or "cover Image").
     * @param multipartFile The image file to be saved.
     * @throws IOException If an error occurs during file saving.
     */
    public static String saveFile(String name, String type, MultipartFile multipartFile) throws IOException {
        if (name == null || type == null || multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Name, type, and non-empty file are required");
        }

        File staticDir = new File(BASE_PATH);
        if (!staticDir.exists()) {
            staticDir.mkdirs();
        }

        String uploadDir;
        if ("profile".equals(type)) {
            uploadDir = PROFILE_IMAGES_PATH;
        } else if ("cover".equals(type)) {
            uploadDir = COVER_IMAGES_PATH;
        } else {
            uploadDir = IMAGES_PATH;
        }


        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String extension = getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String fileNameWithoutExtension = name + "_" + type + "_photo";
        String fileName = fileNameWithoutExtension + "." + extension;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath, fileNameWithoutExtension + ".*")) {
            for (Path existingFile : stream) {
                Files.delete(existingFile);
            }
        } catch (IOException ioException) {
            throw new IOException("Failed to delete existing image file: " + fileNameWithoutExtension, ioException);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException) {
            throw new IOException("Failed to save image file: " + fileName, ioException);
        }
        return uploadPath + "/" + fileName;
    }

    /**
     * Retrieves and encodes the user's profile and cover images as Base64 strings.
     * The encoded images are then set in the provided UserDTO.
     *
     * @param userDTO The UserDTO to populate with image data.
     * @return The updated UserDTO containing Base64-encoded image data.
     */
    public static UserResponseDTO getImage(UserResponseDTO userDTO) {
        MultipartFile profilePic = getProfileImage(userDTO.getUsername());
        //String coverPic = getCoverImage(userDTO.getUserId());

        if (profilePic != null) {
            userDTO.setProfilePicture(profilePic);
        }

        /*if (coverPic != null) {
            userDTO.setCoverImg(coverPic);
        }*/

        return userDTO;
    }

    /**
     * Searches for and retrieves the user's profile image based on their userId.
     * Checks multiple formats (e.g., .jpg, .png, .jpeg) in the user's directory.
     *
     * @param userId The ID of the user.
     * @return The Base64-encoded profile image if found; otherwise, null.
     */
    public static MultipartFile getProfileImage(String userId) {
        try {
            for (String format : ALLOWED_FORMATS) {
                File profilePic = new File(BASE_PATH + userId + "/" + userId + "_profile_photo" + format);
                if (profilePic.exists()) {
                    return convertFileToMultipartFile(profilePic);
                }
            }
        } catch (IOException e) {
            throw new FileConversionException("Failed to convert file to multipart file");
        }

        return null;
    }

    private static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(
                file.getName(),
                file.getName(),
                Files.probeContentType(file.toPath()),
                IOUtils.toByteArray(input)
        );
    }

    /**
     * Searches for and retrieves the user's cover image based on their userId.
     * Checks multiple formats (e.g., .jpg, .png, .jpeg) in the user's directory.
     *
     * @param userId The ID of the user.
     * @return The Base64-encoded cover image if found; otherwise, null.
     */
/*    public static String getCoverImage(Long userId) {

        try {
            for (String format : formats) {
                File coverPic = new File(path + userId + "/" + userId + "_cover_photo" + format);
                if (coverPic.exists()) {
                    return encodeFileToBase64(coverPic);
                }
            }
        } catch (IOException e) {
        }

        return null;
    }*/

    /**
     * Deletes a user's image file based on the specified type (e.g., "profile" or "cover").
     *
     * @param userId The ID of the user.
     * @param type   The type of image to delete (e.g., "profile" or "cover").
     * @throws IOException If an error occurs during file deletion.
     */
    public static void deleteFile(Long userId, String type) throws IOException {
        if (userId == null || type == null) {
            throw new IllegalArgumentException("UserId and type are required");
        }

        String directory;
        if ("profile".equals(type)) {
            directory = PROFILE_IMAGES_PATH;
        } else if ("cover".equals(type)) {
            directory = COVER_IMAGES_PATH;
        } else {
            directory = IMAGES_PATH;
        }

        String fileNameWithoutExtension = userId + "_" + type + "_photo";
        Path dirPath = Paths.get(directory);

        if (!Files.exists(dirPath)) {
            throw new IOException("Directory does not exist: " + directory);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, fileNameWithoutExtension + ".*")) {
            boolean fileDeleted = false;
            for (Path file : stream) {
                Files.delete(file);
                fileDeleted = true;
            }

            if (!fileDeleted) {
                throw new IOException("File not found: " + fileNameWithoutExtension);
            }
        } catch (IOException ioException) {
            throw new IOException("Failed to delete image file: " + fileNameWithoutExtension, ioException);
        }
    }

    /**
     * Encodes a file to a Base64-encoded string.
     *
     * @param file The file to encode.
     * @return The Base64-encoded string.
     * @throws IOException If an error occurs during encoding.
     */
    private static String encodeFileToBase64(File file) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * Extracts the file extension from a given file name.
     *
     * @param fileName The name of the file.
     * @return The file extension as a string.
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}