package paket.maticnaknjigarodjenih.com;

public class Check {
    private static boolean isStringOnlySlova(String str) {
        /*String[] slova = {"A", "a", "B", "b", "C", "c", "Č", "č", "Ć", "ć", "D", "d", "Đ", "đ", "E", "e", "F", "f",
                "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o",
                "P", "p", "R", "r", "S", "s", "Š", "š", "T", "t", "U", "u", "V", "v", "Z", "z", "Ž", "ž", "W", "w", "Q", "q", "Y", "y"};*/
        // izostavio "Dž", "dž", "Lj", "lj", "Nj", "nj"
        if(str != null && str.length() > 0) {
            String slova = " AaBbCcČčĆćDdĐđEeFfGgHhIiJjKkLlMmNnOoPpRrSsŠšTtUuVvZzŽžWwQqYy";
            String[] string = str.split("");

            for (int i = 0; i < string.length; i++) {
                if (!slova.contains(string[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    private static boolean isStringOnlyBrojevi(String str) {
        if(str != null && str.length() > 0) {

            String brojevi = " 0123456789";
            String[] string = str.split("");

            for (int i = 0; i < string.length; i++) {
                if (!brojevi.contains(string[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    private static boolean isStringOnlyBrojeviIslova(String str) {
        if(str != null && str.length() > 0) {

            String brojeviSlova = " 0123456789AaBbCcČčĆćDdĐđEeFfGgHhIiJjKkLlMmNnOoPpRrSsŠšTtUuVvZzŽžWwQqYy";
            String[] string = str.split("");

            for (int i = 0; i < string.length; i++) {
                if (!brojeviSlova.contains(string[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public static String validateData1(String[] podatak) {
        String poruka = "";

        String imeDeteta = podatak[0];
        String prezimeDeteta = podatak[1];
        String jmbgDeteta = podatak[2];
        String polDeteta = podatak[3];
        //String vremeRodjenjaDeteta = podatak[4];
       //String datumRodjenjaDeteta = podatak[5];
        String mestoRodjenjaDeteta = podatak[6];
        String opstinaRodjenjaDeteta = podatak[7];
        String drzavaRodjenjaDeteta = podatak[8];
        String drzavljanstvoDeteta = podatak[9];
        String imeOca = podatak[10];
        String prezimeOca = podatak[11];
        String jmbgOca = podatak[12];
        String polOca = podatak[13];
        //String vremeRodjenjaOca = podatak[14];
        //String datumRodjenjaOca = podatak[15];
        String mestoRodjenjaOca = podatak[16];
        String opstinaRodjenjaOca = podatak[17];
        String drzavaRodjenjaOca = podatak[18];
        String drzavljanstvoOca = podatak[19];
        String prebivalisteOca = podatak[20];
        String adresaOca = podatak[21];
        String imeMajke = podatak[22];
        String prezimeMajke = podatak[23];
        String jmbgMajke = podatak[24];
        String polMajke = podatak[25];
        //String vremeRodjenjaMajke = podatak[26];
        //String datumRodjenjaMajke = podatak[27];
        String mestoRodjenjaMajke = podatak[28];
        String opstinaRodjenjaMajke = podatak[29];
        String drzavaRodjenjaMajke = podatak[30];
        String drzavljanstvoMajke = podatak[31];
        String prebivalisteMajke = podatak[32];
        String adresaMajke = podatak[33];

        if(!isStringOnlySlova(imeDeteta)) {
            poruka += "Polje 'Ime deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prezimeDeteta)) {
            poruka += "Polje 'Prezime deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojevi(jmbgDeteta) || !(jmbgDeteta.length() == 13)) {
            poruka += "Polje 'JMBG deteta' mora da sadrži samo brojeve i to 13 brojeva.\n";
        }
        if(!(polDeteta.equals("m") || polDeteta.equals("z") || polDeteta.equals("ž"))) {
            poruka += "Polje 'Pol deteta' mora da sadrži vrednost 'm' ili 'z'/'ž'.\n";
        }
        if(!isStringOnlySlova(mestoRodjenjaDeteta)) {
            poruka += "Polje 'Mesto rođenja deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(opstinaRodjenjaDeteta)) {
            poruka += "Polje 'Opština rođenja deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavaRodjenjaDeteta)) {
            poruka += "Polje 'Država rođenja deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavljanstvoDeteta)) {
            poruka += "Polje 'Državavljanstvo deteta' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(imeOca)) {
            poruka += "Polje 'Ime oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prezimeOca)) {
            poruka += "Polje 'Prezime oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojevi(jmbgOca) || !(jmbgOca.length() == 13)) {
            poruka += "Polje 'JMBG oca' mora da sadrži samo brojeve i to 13 brojeva.\n";
        }
        if(!(polOca.equals("m") || polOca.equals("z") || polOca.equals("ž"))) {
            poruka += "Polje 'Pol oca' mora da sadrži vrednost 'm' ili 'z'/'ž'.\n";
        }
        if(!isStringOnlySlova(mestoRodjenjaOca)) {
            poruka += "Polje 'Mesto rođenja oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(opstinaRodjenjaOca)) {
            poruka += "Polje 'Opština rođenja oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavaRodjenjaOca)) {
            poruka += "Polje 'Država rođenja oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavljanstvoOca)) {
            poruka += "Polje 'Državavljanstvo oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prebivalisteOca)) {
            poruka += "Polje 'Prebivalište oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojeviIslova(adresaOca)) {
            poruka += "Polje 'Adresa oca' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(imeMajke)) {
            poruka += "Polje 'Ime majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prezimeMajke)) {
            poruka += "Polje 'Prezime majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojevi(jmbgMajke) || !(jmbgMajke.length() == 13)) {
            poruka += "Polje 'JMBG majke' mora da sadrži samo brojeve i to 13 brojeva.\n";
        }
        if(!(polMajke.equals("m") || polMajke.equals("z") || polMajke.equals("ž"))) {
            poruka += "Polje 'Pol majke' mora da sadrži vrednost 'm' ili 'z'/'ž'.\n";
        }
        if(!isStringOnlySlova(mestoRodjenjaMajke)) {
            poruka += "Polje 'Mesto rođenja majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(opstinaRodjenjaMajke)) {
            poruka += "Polje 'Opština rođenja majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavaRodjenjaMajke)) {
            poruka += "Polje 'Država rođenja majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(drzavljanstvoMajke)) {
            poruka += "Polje 'Državavljanstvo majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prebivalisteMajke)) {
            poruka += "Polje 'Prebivalište majke' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojeviIslova(adresaMajke)) {
            poruka += "Polje 'Adresa majke' mora da sadrži samo slova.\n";
        }

        return poruka;
    }

    public static String validateData2(String ime, String prezime, String jmbg, String korisnickoIme, String lozinka, String potvrda) {
        String poruka = "";

        if(!isStringOnlySlova(ime)) {
            poruka += "Polje 'Ime' mora da sadrži samo slova.\n";
        }
        if(!isStringOnlySlova(prezime)) {
            poruka += "Polje 'Prezime mora' da sadrži samo slova.\n";
        }
        if(!isStringOnlyBrojevi(jmbg) || !(jmbg.length() == 13)) {
            poruka += "Polje 'JMBG' mora da sadrži samo brojeve i to 13 brojeva.\n";
        }
        if(!(korisnickoIme.length() > 5)) {
            poruka += "Polje 'Korisničko ime' mora da bude minimalne dužine 5.\n";
        }
        if(!isStringOnlyBrojeviIslova(lozinka) || !(lozinka.length() > 8)) {
            poruka += "Polje 'Lozinka' mora da bude minimalne dužine 8.\n";
        }
        if(!lozinka.equals(potvrda)) {
            poruka += "Polje 'Lozinka' i polje 'Potvrda' ne smeju biti različita.\n";
        }

        return poruka;
    }

}
