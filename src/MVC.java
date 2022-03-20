public class MVC {
    public static void main(String[] args) {
        View1 view = new View1();
        ViewRegistration r_view = new ViewRegistration();
        ViewAdmin a_view = new ViewAdmin();
        ViewUser viewUser = new ViewUser();
        Controller controller = new Controller(view, r_view, a_view, viewUser);
    }
}
