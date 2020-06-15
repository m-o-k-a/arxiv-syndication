package app.arxivorg.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {

    /**
     *
     * @param url
     * @param path : chemin absolu du dossier de destination du fichier ( pas de / à la fin)
     */
    public Downloader(String url, String path) {

        try {
            URL racine = new URL(url);
            getFile(racine, path);
        } catch (MalformedURLException e) {
            System.err.println(url + " : URL non comprise.");
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void getFile(URL u, String path) throws IOException {
        URLConnection uc = u.openConnection();
        String FileType = uc.getContentType();
        int FileLenght = uc.getContentLength();
        if (FileLenght == -1) {
            throw new IOException("Fichier non valide.");
        }
        InputStream in = uc.getInputStream();
        String FileName = u.getFile();
        FileName = FileName.substring(FileName.lastIndexOf('/') + 1);
        FileOutputStream WritenFile = new FileOutputStream(path + '/' + FileName);
        byte[]buff = new byte[1024];
        int l = in.read(buff);
        while(l>0)
        {
            WritenFile.write(buff, 0, l);
            l = in.read(buff);
        }
        WritenFile.flush();
        WritenFile.close();

    }

}
