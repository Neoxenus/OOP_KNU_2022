package my.XML;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Medicines {
    protected List<Medicines.Medicine> medicineList = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Medicine implements Comparable<Medicine>{
        private String name;
        private String pharm;
        private String group;
        private List<String> analogs;
        private Versions versions;

        @Override
        public int compareTo(Medicine that) {
            if (this.getName() == null && that.getName() == null) return 0;
            if (this.getName() == null) return -1;
            if (that.getName() == null) return 1;
            int nameComparison = this.getName().compareTo(that.getName());
            if (nameComparison != 0) return nameComparison < 0 ? -1 : 1;

            if (this.getPharm() == null && that.getPharm() == null) return 0;
            if (this.getPharm() == null) return -1;
            if (that.getPharm() == null) return 1;
            int pharmComparison = this.getPharm().compareTo(that.getPharm());
            if (pharmComparison != 0) return pharmComparison < 0 ? -1 : 1;

            if (this.getGroup() == null && that.getGroup() == null) return 0;
            if (this.getGroup() == null) return -1;
            return that.getGroup() == null ? 1 : this.getGroup().compareTo(that.getGroup());
        }


        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Versions{
            protected List<Version> versionList = new ArrayList<>();
            @Getter
            @Setter
            public static class Version{
                Certificate certificate;
                Package aPackage;
                String dosage;
                @Getter
                @Setter
                public static class Certificate{
                    protected String number;
                    protected String dataFrom;
                    protected String dataTo;
                }
                @Setter
                @Getter
                public static class Package{
                    protected String type;
                    protected int numberInPackage;
                    protected String priceForPackage;
                }
            }
        }
    }
}
