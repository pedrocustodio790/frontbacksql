
const API_BASE_URL = 'http://localhost:8080/api'; // Altere para sua URL

class EstoqueAPI {
    static async listarProdutos() {
        const response = await fetch(`${API_BASE_URL}/produtos`);
        if (!response.ok) {
            throw new Error('Erro ao carregar produtos');
        }
        const data = await response.json();
        return data.map(p => Produto.fromJSON(p));
    }

    static async adicionarProduto(produto) {
        const response = await fetch(`${API_BASE_URL}/produtos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(produto.toJSON())
        });
        if (!response.ok) {
            throw new Error('Erro ao adicionar produto');
        }
        return await response.json();
    }

    static async atualizarProduto(id, produto) {
        const response = await fetch(`${API_BASE_URL}/produtos/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(produto.toJSON())
        });
        if (!response.ok) {
            throw new Error('Erro ao atualizar produto');
        }
        return await response.json();
    }

    static async removerProduto(id) {
        const response = await fetch(`${API_BASE_URL}/produtos/${id}`, {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error('Erro ao remover produto');
        }
    }

    static async adicionarEstoque(id, quantidade) {
        const response = await fetch(`${API_BASE_URL}/produtos/${id}/adicionar?quantidade=${quantidade}`, {
            method: 'POST'
        });
        if (!response.ok) {
            throw new Error('Erro ao adicionar estoque');
        }
    }

    static async removerEstoque(id, quantidade) {
        const response = await fetch(`${API_BASE_URL}/produtos/${id}/remover?quantidade=${quantidade}`, {
            method: 'POST'
        });
        if (!response.ok) {
            throw new Error('Erro ao remover estoque');
        }
    }
}