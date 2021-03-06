package Code;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;

public class Entreprise implements Payable {
    protected TreeSet<Employe> entreprise;
    protected ArrayList<String> idEmployes;
    protected ArrayList<Employe> employeSto;
    protected ArrayList<Employe> verifMatricules;
    protected String fileNom;

    public Entreprise() {
        this.entreprise = new TreeSet<Employe>();
        this.employeSto = new ArrayList<Employe>();
        this.idEmployes = new ArrayList<String>();
        this.verifMatricules = new ArrayList<>();
        this.fileNom = "UNDEFINED";
    }

    //Methode de création du fichier de sauvegarde ou de récupération des données
    protected String creationFichier() throws IOException, EntrepriseException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenue dans la création de votre entreprise : entrez le nom du fichier souhaité");
        String file = sc.nextLine();
        file = file+".csv";
        fileNom = file;
        File f = new File(file);
        //Création du fichier
        if(f.createNewFile()){
            System.out.println("Le fichier à été créé au chemin : "+f.getAbsolutePath());
            ecritureFichier("Id;Nom;Prénom;Matricule;IndiceSalaire\n", f.getAbsolutePath());
        }
        //Récupération des données
        else{
            System.out.println("Le fichier existe dèjà");
            lectureFichier(f.getAbsolutePath());
        }
        return f.getAbsolutePath();
    }

    //Méthode d'écriture dans le fichier : newLine est la nouvelle ligne et file est le chemin ou l'on doit écrire
    private void ecritureFichier(String newLine, String file) throws IOException {
        Path sauvegarde = Paths.get(file);
        Files.write(sauvegarde, String.format(newLine).getBytes(), APPEND);
    }

    private void lectureFichier(String path) throws FileNotFoundException, EntrepriseException {
        Scanner sc;
        sc = new Scanner(new File(path));
        String ligne = sc.nextLine();
        while (sc.hasNext()) {
            ligne = sc.nextLine();
            String[] stoken = ligne.split(";");
            String id = stoken[0];
            String nom = stoken[1];
            String prenom = stoken[2];
            int matricule = Integer.parseInt(stoken[3]);
            float indice = Integer.parseInt(stoken[4]);
            int volumeMensuelVente = 0;
            if(stoken.length > 5){
                volumeMensuelVente = Integer.parseInt(stoken[5]);
            }
            this.idEmployes.add(id);

            if (id.charAt(id.length() - 2) == ('E')) {
                EmployeDeBase employe = new EmployeDeBase(id, nom, prenom, matricule, indice);
                this.entreprise.add(employe);
                this.verifMatricules.add(employe);
                for (Employe e : verifMatricules) {
                    if (e.matricule == matricule && !(e.nom.equals(nom))) {
                        throw new EntrepriseException("Le matricule existe déjà: " + employe);
                    }
                }
            } else if (id.charAt(id.length() - 2) == ('C')) {
                Commercial commercial = new Commercial(id, nom, prenom, matricule, indice, volumeMensuelVente);
                this.entreprise.add(commercial);
                this.verifMatricules.add(commercial);
                for (Employe e : verifMatricules) {
                    if (e.matricule == matricule && !(e.nom.equals(nom))) {
                        throw new EntrepriseException("Le matricule existe déjà: " + commercial);
                    }
                }
            } else {
                Responsable respo = new Responsable(id, nom, prenom, matricule, indice);
                this.entreprise.add(respo);
                this.verifMatricules.add(respo);
                for (Employe e : verifMatricules) {
                    if (e.matricule == matricule && !(e.nom.equals(nom))) {
                        throw new EntrepriseException("Le matricule existe déjà: " + respo);
                    }
                }
            }

        }

    }

    //Méthode de création de l'entreprise
    public void creationEntreprise() throws IOException, EntrepriseException {
        //Initialisation des variables nécessaires à la création ou modification de l'entreprise
        fileNom = creationFichier();
        int cptR = 0;
        int cptRNiveau = 1;
        int cptE = 1;
        int cptC = 1;

        //Boucle de création des Responsable de niveau (Gérant de l'entreprise)
        boolean continu = true;
        if(!entreprise.isEmpty()){
            while (continu) {
                System.out.println("Modifier le Responsable de niveau " + 1 + "," + cptRNiveau + " (tapez R)");
                System.out.println("Passer au niveau suivant (tapez S)");
                Scanner sc = new Scanner(System.in);
                if (sc.nextLine().equals("R")) {

                    //Apelle la création de la branche sous le responsable 1,1
                       modificationBranche(cptR + 1, cptE, cptC, 1, cptRNiveau);
                    cptRNiveau += 1;
                } else if (sc.nextLine().equals("S")) {
                    cptR = 1;
                    modificationBranche(cptR + 1, cptE, cptC, 1, cptRNiveau);
                    continu = true;
                } else{
                    continu = false;
                }
            }
            System.out.println(entreprise);
        }
        else {
            while (continu) {
                System.out.println("Créer un : Responsable de niveau " + 1 + "," + cptRNiveau + " (tapez R)");
                Scanner sc = new Scanner(System.in);
                if (sc.nextLine().equals("R")) {
                    cptR = 1;
                    String id = "R" + cptR + "," + cptRNiveau;
                    entreprise.add(creationResponsable(id));

                    //Apelle la création de la branche sous le responsable 1,1
                    creationBranche(cptR + 1, cptE, cptC, 1, cptRNiveau);
                    cptRNiveau += 1;
                } else {
                    continu = false;
                }
            }
        }
        //Liste avec index pour pouvoir break la deuxième for du rangement dans les listes de responsables.
        for(Employe e: entreprise){
            employeSto.add(e);
        }
        for(Employe r: entreprise){
            if(r.id.length() <= 4) {
                Responsable r1 = (Responsable) r;
                int niveau = r.id.charAt(1);
                for (int i=0; i<entreprise.size(); i++){
                    Employe e = employeSto.get(i);
                    int eNiveau = e.id.charAt(1);
                    if(e != r){
                        if(e.id.length() <= 4 && e.id.charAt(1) == niveau){
                            i = entreprise.size();
                        }else{
                            r1.addEmployee(e);
                        }
                    }
                }
            }
        }
    }

    //Création d'une branche sous un responsable de manière récursive
    public boolean creationBranche(int cptR, int cptE, int cptC, int cptRNiveau, int cptRBranche) throws IOException {
        boolean branche = true;
        //Permet de remonter lorsque l'on a finis la création de la branche
        if(cptR == 1){
            return false;
        }
        while(branche) {
            //cptRNiveau = recupCptRespNiveau(cptR);
            Scanner sc = new Scanner(System.in);
            System.out.println("Créer un : Responsable de niveau " + (cptR) + " (tapez R), un employee sous Responsable " + (cptR-1) + " (tapez E), un commercial sous Responsable " + (cptR-1) + " (tapez C), branche terminée (tapez Q)");
            String rep = sc.nextLine();
            if (rep.equals("R")) {
                String id = "R" + cptR + "," + cptRNiveau;
                cptRNiveau += 1;
                entreprise.add(creationResponsable(id));
                //Création d'une sous branche avec un nouveau responsable (récursivité de la création)
                return (creationBranche(cptR+1, cptE, cptC, 1, cptRNiveau));
            }
            //Crée un employé de base
            else if (rep.equals("E")) {
                int cptRLoc = cptR - 1;
                String id = "R"+cptRLoc+","+(cptRBranche-1)+"E"+cptE;
                entreprise.add(creationEmploye(id));
                cptE += 1;
            }
            //Crée un commercial
            else if (rep.equals("C")) {
                int cptRLoc = cptR - 1;
                String id = "R"+cptRLoc+","+(cptRBranche-1)+"C"+cptC;
                entreprise.add(creationCommercial(id));
                cptC += 1;
            }
            //L'utilisateur demande à quitter
            else if(rep.equals("Q")){
                return creationBranche(cptR-1, cptE, cptC, cptRNiveau+1, cptRBranche);
            }
        }
        return true;
    }

    //Création d'une branche sous un responsable de manière récursive
    public boolean modificationBranche(int cptR, int cptE, int cptC, int cptRNiveau, int cptRBranche) throws IOException {
        boolean branche = true;
        //Permet de remonter lorsque l'on a finis la création de la branche
        if(cptR == 1){
            return false;
        }
        while(branche) {
            //cptRNiveau = recupCptRespNiveau(cptR);
            Scanner sc = new Scanner(System.in);
            System.out.println("Modifier le : le(s) employe(s) sous Responsable " + (cptR-1) + " (tapez E), le(s) commercial sous Responsable " + (cptR-1) + " (tapez C), branche terminée (tapez Q)");
            String rep = sc.nextLine();

            //Crée un employé de base
            if (rep.equals("E")) {
                int cptRLoc = cptR - 1;
                String id = "R"+cptRLoc+","+(cptRBranche)+"E"+cptE;
                entreprise.add(creationEmploye(id));
                cptE += 1;
            }
            //Crée un commercial
            else if (rep.equals("C")) {
                int cptRLoc = cptR - 1;
                String id = "R"+cptRLoc+","+(cptRBranche)+"C"+cptC;
                entreprise.add(creationCommercial(id));
                cptC += 1;
            }
            //L'utilisateur demande à quitter
            else if(rep.equals("Q")){
                return creationBranche(cptR-1, cptE, cptC, cptRNiveau+1, cptRBranche);
            }
        }
        return true;
    }

    //Fonction permettant la création d'un responsable
    public Responsable creationResponsable(String id) throws IOException {
        Scanner sc = new Scanner(System.in);
        // Processus de création
        System.out.print("Nom du Responsable : ");
        String nom = sc.nextLine();
        System.out.print("Prénom du Responsable : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule du Responsable (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire du Responsable (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        //Création du responsable et écriture dans le fichier de sauvegarde
        Responsable r = new Responsable(id, nom, prenom, matricule, indiceSalaire);
        String newLine = id+";"+nom+";"+prenom+";"+matricule+";"+indiceSalaire+"\n";
        ecritureFichier(newLine, fileNom);
        return r;
    }

    //Fonction permettant la création d'un employe de base
    public EmployeDeBase creationEmploye(String id) throws IOException {
        Scanner sc = new Scanner(System.in);
        // Processus de création
        System.out.print("Nom de l'Employe : ");
        String nom = sc.nextLine();
        System.out.print("Prénom de l'Employe : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule de l'Employe (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire de l'Employe (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        //Création de l'employé et écriture dans le fichier de sauvegarde
        EmployeDeBase e = new EmployeDeBase(id, nom, prenom, matricule, indiceSalaire);
        String newLine = id+";"+nom+";"+prenom+";"+matricule+";"+indiceSalaire+"\n";
        ecritureFichier(newLine, fileNom);
        return e;
    }

    //Fonction permettant la création d'un commercial sans son volume de vente
    public Commercial creationCommercial(String id) throws IOException {
        Scanner sc = new Scanner(System.in);
        // Processus de création
        System.out.print("Nom du Commercial : ");
        String nom = sc.nextLine();
        System.out.print("Prénom du Commercial : ");
        String prenom = sc.nextLine();
        System.out.print("Matricule du Commercial (en chiffre): ");
        int matricule = sc.nextInt();
        System.out.print("Indice salaire du Commercial (en chiffre): ");
        int indiceSalaire = sc.nextInt();
        System.out.print("Volume de vente du Commercial (en chiffre): ");
        int volumeVente = sc.nextInt();
        //Création de l'employé et écriture dans le fichier de sauvegarde
        Commercial c = new Commercial(id, nom, prenom, matricule, indiceSalaire, volumeVente);
        String newLine = id+";"+nom+";"+prenom+";"+matricule+";"+indiceSalaire+";"+volumeVente+"\n";
        ecritureFichier(newLine, fileNom);
        return c;
    }

    //Methode de calcul des salaires totaux que l'entreprise reverse
    public float CalculSalaire(){
        float salaireTotal = 0;
        for (Employe e : entreprise){
            float salaire = e.CalculSalaire();
            System.out.println("Salaire de "+e+" : "+salaire);
            salaireTotal += salaire;
        }
        return salaireTotal;
    }

    public ArrayList<Employe> affichageHierarchique(int matricule){
        String aff = "";
        int compteur = 0;
        boolean firstFind = false;
        boolean firstEnd = true;
        ArrayList<Employe> branche = new ArrayList<Employe>();

        for(Employe e: entreprise){
            if(e.getId().length() == 4){
                if(firstFind) {
                    if(e.getId().charAt(1) == branche.get(0).getId().charAt(1)) {
                        firstEnd = false;
                    }
                }
            }
            if(e.getMatricule() == matricule){
                firstFind = true;
            }
            if(firstFind && firstEnd){
                for(Employe b: entreprise){
                    if(e.getId().length() == 4){
                        branche.add(b);
                    }
                    else{
                        if(e.getId().charAt(4) == 'E'){
                            branche.add(b);
                        }
                        if(e.getId().charAt(4) == 'C'){
                            branche.add(b);
                        }
                    }
                }
                aff += e.toString();
            }
            compteur++;
        }
        System.out.println(aff);
        return branche;
    }

    public void affichageHierarchiqueDirect(int matricule){
        String aff = "";
        int compteur = 0;
        boolean firstFind = false;
        boolean firstEnd = true;
        String compare = "";
        ArrayList<Employe> branche = new ArrayList<Employe>();

        for(Employe e: entreprise){
            if(e.getId().length() == 4){
                if(firstFind) {
                    if(e.getId().charAt(1) == branche.get(0).getId().charAt(1)) {
                        firstEnd = false;
                    }
                }
            }
            if(e.getMatricule() == matricule){
                firstFind = true;
                compare = e.getId();
            }
            if(firstFind && firstEnd){
                for(Employe b: entreprise){
                    if(e.getId().length() == 4){
                        branche.add(b);
                    }
                    else{
                        if(e.getId().charAt(4) == 'E' && e.getId().substring(0,4).equals(compare)){
                            branche.add(b);
                        }
                        if(e.getId().charAt(4) == 'C' && e.getId().substring(0,4).equals(compare)){
                            branche.add(b);
                        }
                    }
                }
                aff += e.toString();
            }
            compteur++;
        }
        System.out.println(aff);
    }

    @Override
    public String toString() {
        String aff = "L'entreprise est composée de :";
        for(Employe e: entreprise){
            aff += e.toString();
        }
        return aff;
    }

    public int calculSalaire() {
        int salaireTotal = 0;
        for (Employe e: entreprise){
            salaireTotal += e.CalculSalaire();
        }
        return salaireTotal;
    }

    public int getMatriculeEmploye(String nom, String prenom) throws EntrepriseException{
        for(Employe e: entreprise){
            if(e.getNom().equals(nom)){
                if (e.getPrenom().equals(prenom)){
                    return e.getMatricule();
                }
                throw new EntrepriseException("Le prénom n'est pas valide ou n'existe pas" + prenom);
            }
            if(e.getPrenom().equals(prenom)){
                if (e.getNom().equals(nom)){
                    return e.getMatricule();
                }
                throw new EntrepriseException("Le nom n'est pas valide ou n'existe pas" + prenom);
            }
        }
        throw new EntrepriseException("L'employé n'est pas valide ou n'existe pas");
    }

    public void suppression(int mat) throws IOException {
        Path sauvegarde = Paths.get("myTempFile.csv");

        Scanner sc;
        sc = new Scanner(new File(fileNom));
        String content = "";

        while (sc.hasNext()) {
            String ligne = sc.nextLine();
            String[] stoken = ligne.split(";");
            String id = stoken[0];
            String nom = stoken[1];
            String prenom = stoken[2];
            int matricule = Integer.parseInt(stoken[3]);
            int indice = Integer.parseInt(stoken[4]);
            if(matricule != mat) {
                String newLine = id + ";" + nom + ";" + prenom + ";" + matricule + ";" + indice + "\n";
                content += newLine;
            }
        }
        Files.write(sauvegarde, Collections.singleton(content), StandardCharsets.UTF_8);

        Path path = Paths.get(fileNom);

        sc = new Scanner(new File("myTempFile.csv"));
        String lignes = "";

        while (sc.hasNext()) {
            String ligne = sc.nextLine();
            String[] stoken = ligne.split(";");
            String id = stoken[0];
            String nom = stoken[1];
            String prenom = stoken[2];
            int matricule = Integer.parseInt(stoken[3]);
            int indice = Integer.parseInt(stoken[4]);
            String newLine = id + ";" + nom + ";" + prenom + ";" + matricule + ";" + indice + "\n";
            lignes += newLine;
        }
        Files.write(path, Collections.singleton(lignes), StandardCharsets.UTF_8);
    }
}
