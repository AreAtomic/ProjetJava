package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Entreprise {
    protected TreeMap<String, Employe> entreprise;
    protected ArrayList<String> idEmployes;
    protected ArrayList<Employe> verifMatricules;

    public Entreprise() throws EntrepriseException, FileNotFoundException {
        this.entreprise = new TreeMap<String, Employe>();
        this.idEmployes = new ArrayList<String>();
        this.verifMatricules = new ArrayList<>();

        Scanner sc;
            sc = new Scanner(new File("C:\\Users\\aurel\\Documents\\ProjetJava\\Code\\src\\Code\\Entreprise.csv"));
            String ligne = sc.nextLine();
            while (sc.hasNext()) {
                ligne = sc.nextLine();
                String[] stoken = ligne.split(",");
                String id = stoken[0];
                String nom = stoken[1];
                String prenom = stoken[2];
                int matricule = Integer.parseInt(stoken[3]);
                float indice = Integer.parseInt(stoken[4]);
                this.idEmployes.add(id);

                if (id.charAt(id.length() - 2) == ('E')) {
                    EmployeDeBase employe = new EmployeDeBase(nom, prenom, matricule, indice);
                    this.entreprise.put(id, employe);
                    this.verifMatricules.add(employe);
                    for (Employe e : verifMatricules) {
                        if (e.matricule == matricule && !(e.nom.equals(nom))) {
                            throw new EntrepriseException("Le matricule existe déjà: " + employe);
                        }
                    }
                } else if (id.charAt(id.length() - 2) == ('C')) {
                    Commercial commercial = new Commercial(nom, prenom, matricule, indice);
                    this.entreprise.put(id, commercial);
                    this.verifMatricules.add(commercial);
                    for (Employe e : verifMatricules) {
                        if (e.matricule == matricule && !(e.nom.equals(nom))){
                            throw new EntrepriseException("Le matricule existe déjà: " + commercial);
                        }
                    }
                } else {
                    Responsable respo = new Responsable(nom, prenom, matricule, indice);
                    this.entreprise.put(id, respo);
                    this.verifMatricules.add(respo);
                    for (Employe e : verifMatricules) {
                        if (e.matricule == matricule && !(e.nom.equals(nom))) {
                            throw new EntrepriseException("Le matricule existe déjà: " + respo);
                        }
                    }
                }

        }

    }
}