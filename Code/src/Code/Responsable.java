package Code;import java.util.TreeSet;public class Responsable extends Employe{    private TreeSet<Employe> lesEmployes;    public Responsable(String nom, String prenom, int matricule, int indiceSalaire, TreeSet<Employe> lesEmployes) {        super(nom, prenom, matricule, indiceSalaire);        this.lesEmployes = lesEmployes;    }    @Override    public String toString() {        return "Responsable{" +                "lesEmployes=" + lesEmployes +                '}';    }    public  int CalculSalaire(){        return 1000;    }}