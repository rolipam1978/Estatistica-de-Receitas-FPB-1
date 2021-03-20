
import java.sql.*;
import java.util.Scanner;

public class EstatisticaDeReceitas {

    final static String url = "jdbc:postgresql://192.168.0.2:5432/fciapopular";
    final static String user = "pod1";
    final static String senha = "pod1";
    static String sql;
    static String total;
    static String opcao;
    static String dataini;
    static String datafim;
    static Scanner sc;

    public static void main(String[] args) {

        do {
            System.out.println("Estatistica de Receitas");
            System.out.println("Opção 1 - SUS");
            System.out.println("Opção 2 - Particular");
            System.out.println("Opção 3 - Total");
            System.out.println("Opção 4 - Sair");

            sc = new Scanner(System.in);

            System.out.println("");
            System.out.printf("Digite a Opção: ");
            opcao = sc.nextLine();
            if (opcao.equals("4")) {
                System.exit(0);
            } else if (opcao.equals("1") || opcao.equals("2") || opcao.equals("3")) {
                System.out.printf("Digite a Data Inicial da Consulta (aaaa-mm-dd): ");
                dataini = sc.nextLine();
                System.out.printf("Digite a Data Final da Consulta (aaaa-mm-dd): ");
                datafim = sc.nextLine();
            } else {
                System.out.println("Digite uma das Opções Válidas!");
            }

            if (opcao.equals(
                    "1") || opcao.equals("2")) {
                sql = "select distinct(dat_emissao), count(flg_receita) from cadcvend where "
                        + "dat_emissao between '" + dataini + "' and '" + datafim + "' and flg_receita='" + opcao + "' and flg_excluido is null "
                        + "group by dat_emissao order by dat_emissao";

                total = "select count(flg_receita) from cadcvend where dat_emissao between '" + dataini + "' "
                        + "and '" + datafim + "' and flg_receita='" + opcao + "' and flg_excluido is null";

            } else if (opcao.equals(
                    "3")) {
                sql = "select distinct(dat_emissao), count(flg_receita) from cadcvend where "
                        + "dat_emissao between '" + dataini + "' and '" + datafim + "' and flg_receita is not null and flg_excluido is null "
                        + "group by dat_emissao order by dat_emissao";

                total = "select count(flg_receita) from cadcvend where dat_emissao between '" + dataini + "' "
                        + "and '" + datafim + "' and flg_receita is not null and flg_excluido is null";
            }

            //System.out.println(sql);


            try {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection(url, user, senha);
                Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stm.executeQuery(sql);


                if (rs.first()) {

                    if (opcao.equals("1")) {
                        System.out.println("");
                        System.out.println("Receitas SUS");
                        System.out.println("Data\t\t\tQuantidade");
                        System.out.println("-----------------------------------------------------");
                        System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        while (rs.next()) {
                            System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        }
                        System.out.println("-----------------------------------------------------");
                        ResultSet result = stm.executeQuery(total);
                        if (result.first()) {
                            System.out.println("Total de Receitas SUS: " + result.getInt("count"));
                            con.close();
                        }
                    } else if (opcao.equals("2")) {
                        System.out.println("");
                        System.out.println("Receitas Particulares");
                        System.out.println("Data\t\t\tQuantidade");
                        System.out.println("-----------------------------------------------------");
                        System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        while (rs.next()) {
                            System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        }
                        System.out.println("-----------------------------------------------------");
                        ResultSet result = stm.executeQuery(total);
                        if (result.first()) {
                            System.out.println("Total de Receitas Particulares: " + result.getInt("count"));
                            con.close();
                        }
                    } else if (opcao.equals("3")) {
                        System.out.println("");
                        System.out.println("Total de Receitas");
                        System.out.println("Data\t\t\tQuantidade");
                        System.out.println("-----------------------------------------------------");
                        System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        while (rs.next()) {
                            System.out.println(rs.getString("dat_emissao") + "\t\t" + rs.getInt("count"));
                        }
                        System.out.println("-----------------------------------------------------");
                        ResultSet result = stm.executeQuery(total);
                        if (result.first()) {
                            System.out.println("Total de Receitas SUS e Particulares: " + result.getInt("count"));
                            con.close();
                        }
                    } else {
                        System.out.println("Não Consegui Pegar os Dados!");
                    }

                } else {
                    System.out.println("Não Foi possivel localizar os dados");
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        while (!opcao.equals("4"));
    }
    
}