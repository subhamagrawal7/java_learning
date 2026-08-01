import java.util.Objects;

class Badge {
    public String print(Integer id, String name, String department) {
        String idEmployee = (id != null) ? "[" + Objects.toString(id) + "] - " : "";
        String departmentEmployee = (department != null) ? department.toUpperCase() : "OWNER";
        return idEmployee + name + " - " + departmentEmployee;
    }
}
