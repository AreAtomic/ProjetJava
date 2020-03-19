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
        Scanner sc;
        sc = new Scanner(new File("C:\\Users\\cacar\\Documents\\ProjetJavaVrai\\Code\\src\\Code\\Entreprise.csv"));
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
                    if (e.matricule == matricule && !(e.nom.equals(nom))) {
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

    public void creation() {
        boolean continu = true;
        int cptR = 1;
        System.out.println("Bienvenu dans la création de l'entreprise");
        Scanner sc = new Scanner(System.in);
        while (continu) {
            System.out.println("Combien de responsable(s) en haut de la hierarchie de l'entreprise? ");
            int reponse = sc.nextInt();
            if (reponse == 1) {
                Employe R = creationResponsable();
                String responsable = "R1";
                entreprise.put(responsable, R);
                boolean emp = true;
                while (emp) {
                    int cptEmploye = 0;
                    int cptCommercial = 0;
                    int cptRNiveau = 0;
                    String employe = "";
                    sc.nextLine();
                    System.out.println("Employe sous le Reponsable (E), Commercial sous le Responsable (C), Responsable sous le Responsable (R), entrer N pour quitter : ");
                    String rep = sc.nextLine();
                    if (rep.equals("N")) {
                        emp = false;
                        continu = false;
                        System.out.println("L'entreprise créée est :\n"+entreprise);
                    } else if (rep.equals("E")) {
                        Employe E = creationEmployeDeBase();
                        employe = "E" +cptEmploye + 1;
                        entreprise.put(responsable+employe , E);

                    } else if (rep.equals("C")) {
                        Employe C = creationCommercial();
                        entreprise.put(responsable + "C" + cptCommercial, C);
                        cptCommercial += 1;
                    } else if (rep.equals("R")) {
                        cptR = 2;
                        Employe R2 = creationResponsable();
                        cptRNiveau += 1;
                        responsable = "R" + cptR + 1 + "," + cptRNiveau;
                        entreprise.put(responsable, R2);
                        creaBrance(responsable, 1, 1, cptR + 1, true);
                    }

                }
            } else {

            }
        }


    }

    private Responsable creationResponsable() { //Crétion d'un Responsable
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("\nPrenom : ");
        String prenom = sc.nextLine();
        System.out.print("\nMatricule : ");
        int matricule = sc.nextInt();
        System.out.print("\nIndice Salaire : ");
        int indiceSalaire = sc.nextInt();
        return new Responsable(nom, prenom, matricule, indiceSalaire);
    } // methode pour alleger la methode création et modificationEntreprise

    private Commercial creationCommercial() {//création d'un Commercial
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("\nPrenom : ");
        String prenom = sc.nextLine();
        System.out.print("\nMatricule : ");
        int matricule = sc.nextInt();
        System.out.print("\nIndice Salaire : ");
        int indiceSalaire = sc.nextInt();
        return new Commercial(nom, prenom, matricule, indiceSalaire);
    }// methode pour alleger la methode création et modificationEntreprise

    private EmployeDeBase creationEmployeDeBase() { // methode pour créer un Employe de base
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("\nPrenom : ");
        String prenom = sc.nextLine();
        System.out.print("\nMatricule : ");
        int matricule = sc.nextInt();
        System.out.print("\nIndice Salaire : ");
        int indiceSalaire = sc.nextInt();
        return new EmployeDeBase(nom, prenom, matricule, indiceSalaire);
    }// methode pour alleger la methode création et modificationEntreprise

    private void creaBrance(String responsable, int cptEmploye, int cptCommercial, int cptR, boolean continuer) {
        while (continuer) {
            int cptRNiveau = 0;
            Scanner sc = new Scanner(System.in);
            System.out.println("Employe sous le Reponsable" + cptR + " (E), Commercial sous le Responsable" + cptR + " (C), Responsable sous le Responsable" + cptR + " (R), entrer N pour quitter");
            String rep = sc.nextLine();
            if (rep.equals("N")) {
                continuer = false;
            } else if (rep.equals("E")) {
                Employe E = creationEmployeDeBase();
                entreprise.put(responsable + "E" + cptEmploye, E);
                cptEmploye += 1;
            } else if (rep.equals("C")) {
                Employe C = creationCommercial();
                entreprise.put(responsable + "C" + cptCommercial, C);
                cptCommercial += 1;
            } else if (rep.equals("R")) {
                Employe R2 = creationResponsable();
                cptRNiveau += 1;
                responsable = "R" + cptR + "," + cptRNiveau;
                entreprise.put(responsable, R2);
                creaBrance(responsable, 1, 1, cptR + 1, true);
            }
        }
    }// methode pour alleger la methode création et modificationEntreprise

    public float CalculSalaire() {
        float salaireTotal = 0;

        for (String e : idEmployes) {
            float salaire = entreprise.get(e).CalculSalaire();
            System.out.println("Salaire de " + entreprise.get(e) + " : " + salaire);
            salaireTotal += salaire;
        }
        return salaireTotal;
    }
}