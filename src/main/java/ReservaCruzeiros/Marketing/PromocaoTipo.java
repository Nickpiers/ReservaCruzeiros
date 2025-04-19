package ReservaCruzeiros.Marketing;

public enum PromocaoTipo {
    PROMO_2_POR_1("2 por 1", "Reservas de 2 por 1 somente por 30 dias!", "2-por-1", "primeira"),
    UPGRADE_VARANDA("Upgrade varanda", "Reserve uma cabine e ganhe upgrade para varanda grátis! Promoção por tempo limitado.", "varanda", "segunda"),
    DESCONTO_GRUPOS("Desconto para grupos", "Grupos com 4 ou mais pessoas ganham 20% de desconto! Válido até o fim do mês.", "grupos", "terceira"),;

    private final String nome;
    private final String descricao;
    private final String queueName;
    private final String routingKey;

    PromocaoTipo(String nome, String descricao, String queueName, String routingKey) {
        this.nome = nome;
        this.descricao = descricao;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}