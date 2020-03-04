package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Entreprise {
    protected TreeMap<String ,Employe> entreprise;
    protected ArrayList<String> idEmployes;

    public Entreprise() throws EntrepriseException{
        this.entreprise = new TreeMap<String, Employe>();
        this.idEmployes = new ArrayList<String>();

        Scanner sc;
        try {
            sc = new Scanner(new File("C:\\Users\\cacar\\Documents\\ProjetJava\\Code\\src\\Code\\Entreprise.txt"));
            String ligne = sc.nextLine();
            while (sc.hasNext()) {
                ligne = sc.nextLine();
                String[] stoken = ligne.split(",");
                System.out.print(stoken);
                String id = stoken[0];
                String nom = stoken[1];
                String prenom = stoken[2];
                int matricule = Integer.valueOf(stoken[3]);
                float indice = Integer.valueOf(stoken[4]);
                this.idEmployes.add(id);
                if( id.charAt(id.length()-2) == ('E')  ){
                    EmployeDeBase employe = new EmployeDeBase(nom, prenom, matricule, indice);
                    this.entreprise.put(id,employe);
                }else if (id.charAt(id.length()-2) == ('C')){
                    Commercial employe = new Commercial(nom,prenom,matricule,indice);
                    this.entreprise.put(id,employe);
                }
                else{
                    Responsable employe = new Responsable(nom,prenom,matricule,indice);
                    this.entreprise.put(id,employe);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return "Entreprise{" +
                "entreprise=" + entreprise +
                ", idEmployes=" + idEmployes +
                '}';
    }
}
