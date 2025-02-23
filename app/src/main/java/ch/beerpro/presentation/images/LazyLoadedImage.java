package ch.beerpro.presentation.images;

public class LazyLoadedImage {

    private int localPreviewResource;
    private String serverStorageFilename;
    public LazyLoadedImage(int localPreviewResource, String serverStorageFilename) {
        this.localPreviewResource = localPreviewResource;
        this.serverStorageFilename = serverStorageFilename;
    }

    public int getLocalPreviewResource() {
        return localPreviewResource;
    }

    public void setLocalPreviewResource(int localPreviewResource) {
        this.localPreviewResource = localPreviewResource;
    }

    public String getServerStorageFilename() {
        return serverStorageFilename;
    }

    public void setServerStorageFilename(String serverStorageFilename) {
        this.serverStorageFilename = serverStorageFilename;
    }

}