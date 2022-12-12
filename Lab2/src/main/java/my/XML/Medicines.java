package my.XML;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
public class Medicines {
    protected List<Medicines.Medicine> medicineList = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Medicine implements Comparable<Medicine>{
        private String name;
        private String pharm;
        private String group;
        private List<String> analogs;
        private Versions versions;

        @Override
        public int compareTo(Medicine that) {
            if (this.getName() == null && that.getName() == null) {
                // pass
            } else if (this.getName() == null) {
                return -1;
            } else if (that.getName() == null) {
                return 1;
            } else {
                int nameComparison = this.getName().compareTo(that.getName());
                if (nameComparison != 0) {
                    return nameComparison < 0 ? -1 : 1;
                }
            }

            if (this.getPharm() == null && that.getPharm() == null) {
                // pass
            } else if (this.getPharm() == null) {
                return -1;
            } else if (that.getPharm() == null) {
                return 1;
            } else {
                int pharmComparison = this.getPharm().compareTo(that.getPharm());
                if (pharmComparison != 0) {
                    return pharmComparison < 0 ? -1 : 1;
                }
            }

            if (this.getGroup() == null && that.getGroup() == null) {
                // pass
            } else if (this.getGroup() == null) {
                return -1;
            } else if (that.getGroup() == null) {
                return 1;
            } else {
                int groupComparison = this.getGroup().compareTo(that.getGroup());
                if (groupComparison != 0) {
                    return groupComparison < 0 ? -1 : 1;
                }
            }

            if (this.getVersions() == null && that.getVersions() == null) {
                return 0;
            } else if (this.getVersions() == null) {
                return -1;
            } else if (that.getVersions() == null) {
                return 1;
            } else {
                return this.getVersions().compareTo(that.getVersions());
            }
        }


        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Versions implements Comparable<Versions>{
            protected List<Version> versionList = new ArrayList<>();

            @Override
            public int compareTo(Versions that) {

                return  (that.hashCode()) - versionList.hashCode();
            }


            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Version{
                Certificate certificate;
                Package aPackage;
                String dosage;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Certificate implements Comparable<Certificate> {
                    protected String number;
                    protected String dataFrom;
                    protected String dataTo;

                    @Override
                    public int compareTo(Certificate that) {
                        if (this.getNumber() == null && that.getNumber() == null) {
                            // pass
                        } else if (this.getNumber() == null) {
                            return -1;
                        } else if (that.getNumber() == null) {
                            return 1;
                        } else {
                            int numberComparison = this.getNumber().compareTo(that.getNumber());
                            if (numberComparison != 0) {
                                return numberComparison < 0 ? -1 : 1;
                            }
                        }

                        if (this.getDataFrom() == null && that.getDataFrom() == null) {
                            // pass
                        } else if (this.getDataFrom() == null) {
                            return -1;
                        } else if (that.getDataFrom() == null) {
                            return 1;
                        } else {
                            int dataFromComparison = this.getDataFrom().compareTo(that.getDataFrom());
                            if (dataFromComparison != 0) {
                                return dataFromComparison < 0 ? -1 : 1;
                            }
                        }

                        if (this.getDataTo() == null && that.getDataTo() == null) {
                            return 0;
                        } else if (this.getDataTo() == null) {
                            return -1;
                        } else if (that.getDataTo() == null) {
                            return 1;
                        } else {
                            return this.getDataTo().compareTo(that.getDataTo());
                        }
                    }
                }
                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Package implements Comparable<Package> {
                    protected String type;
                    protected int numberInPackage;
                    protected String priceForPackage;

                    @Override
                    public int compareTo(Package that) {
                        if (this.getType() == null && that.getType() == null) {
                            // pass
                        } else if (this.getType() == null) {
                            return -1;
                        } else if (that.getType() == null) {
                            return 1;
                        } else {
                            int typeComparison = this.getType().compareTo(that.getType());
                            if (typeComparison != 0) {
                                return typeComparison < 0 ? -1 : 1;
                            }
                        }

                        if (this.getNumberInPackage() != that.getNumberInPackage()) {
                            return (this.getNumberInPackage() < that.getNumberInPackage() ? -1 : 1);
                        }

                        if (this.getPriceForPackage() == null && that.getPriceForPackage() == null) {
                            return 0;
                        } else if (this.getPriceForPackage() == null) {
                            return -1;
                        } else if (that.getPriceForPackage() == null) {
                            return 1;
                        } else {
                            return this.getPriceForPackage().compareTo(that.getPriceForPackage());
                        }
                    }
                }
            }
        }
    }
}
