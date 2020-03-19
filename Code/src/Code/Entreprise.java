package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Entreprise implements Payable {
    protected TreeMap<String, Employe> entreprise;
    protected ArrayList<String> idEmployes;
    protected ArrayList<Employe> verifMatricules;

    public Entreprise() throws EntrepriseException, FileNotFoundException {
        this.entreprise = new TreeMap<String, Employe>();
        this.idEmployes = new ArrayList<String>();
        this.verifMatricules = new ArrayList<>();

        /*Scanner sc;
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
        }*/
    }

    public void creationEntreprise(){
        int cptR = 0;
        int cptRNiveau = 1;
        int cptE = 1;
        int cptC = 1;
        boolean continu = true;
        while(continu) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Créer un : Responsable de niveau " + 1 + "," + cptRNiveau + " (tapez R)");
            if (sc.nextLine().equals("R")) {
                cptR = 1;
                String id = "R" + cptR + "," + cptRNiveau;
                entreprise.put(id, creationResponsable());
                creationBranche(cptR+1, cptE, cptC, 1);
                cptRNiveau += 1;
            } else {
                continu = false;
            }
        }
    }

    public boolean creationBranche(int cptR, int cptE, int cptC, int cptRNiveau){
        boolean branche = true;
        if(cptR == 1){
            return false;
        }
        while(branche) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Créer un : Responsable de niveau " + (cptR) + " (tapez R), un employee sous Responsable " + (cptR-1) + " (tapez E), un commercial sous Responsable " + (cptR-1) + " (tapez C), branche terminée (tapez Q)");
            String rep = sc.nextLine();
            if (rep.equals("R")) {
                String id = "R" + cptR + "," + cptRNiveau;
                cptRNiveau += 1;
                entreprise.put(id, creationResponsable());
                return (creationBranche(cptR+1, cptE, cptC, 1));
            } else if (rep.equals("E")) {
                String id = "R"+cptR+","+cptRNiveau+"E"+cptE;
                entreprise.put(id, creationEmploye());
                cptE += 1;
            } else if (rep.equals("C")) {
                String id = "R"+cptR+","+cptRNiveau+"C"+cptC;
                entreprise.put(id, creationCommercial());
                cptC += 1;
            } else if(rep.equals("Q")){
               return creationBranche(cptR-1, cptE, cptC, cptRNiveau+1);
            }
        }
        return true;
    }

    //Fonction permettant la création d'un responsable
    public Responsable creationResponsable(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom du Responsable : ");
        String nom = sc.nextLine();
        System.out.print("Prénom du Responsable : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule du Responsable (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire du Responsable (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        Responsable r = new Responsable(nom, prenom, matricule, indiceSalaire);
        return r;
    }

    //Fonction permettant la création d'un employe de base
    public EmployeDeBase creationEmploye(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom de l'Employe : ");
        String nom = sc.nextLine();
        System.out.print("Prénom de l'Employe : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule de l'Employe (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire de l'Employe (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        EmployeDeBase e = new EmployeDeBase(nom, prenom, matricule, indiceSalaire);
        return e;
    }

    //Fonction permettant la création d'un commercial sans son volume de vente
    public Commercial creationCommercial(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom du Commercial : ");
        String nom = sc.nextLine();
        System.out.print("Prénom du Commercial : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule du Commercial (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire du Commercial (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        Commercial c = new Commercial(nom, prenom, matricule, indiceSalaire);
        return c;
    }


    public float CalculSalaire(){
        float salaireTotal = 0;
        for (String e : idEmployes){
            float salaire = entreprise.get(e).CalculSalaire();
            System.out.println("Salaire de "+entreprise.get(e)+" : "+salaire);
            salaireTotal += salaire;
        }
        return salaireTotal;
    }
}