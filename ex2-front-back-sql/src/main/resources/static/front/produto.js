class Produto {
    constructor(id, nome, preco, quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = parseFloat(preco);
        this.quantidade = parseInt(quantidade);
    }

    calcularTotal() {
        return this.preco * this.quantidade;
    }

    toJSON() {
        return {
            id: this.id,
            nome: this.nome,
            preco: this.preco,
            quantidade: this.quantidade
        };
    }

    static fromJSON(json) {
        return new Produto(json.id, json.nome, json.preco, json.quantidade);
    }
}