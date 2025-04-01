document.addEventListener('DOMContentLoaded', () => {
    // Elementos DOM
    const produtoForm = document.getElementById('produtoForm');
    const tabelaProdutos = document.getElementById('tabelaProdutos');
    const valorTotalEstoque = document.getElementById('valorTotalEstoque');
    const editarProdutoModal = new bootstrap.Modal(document.getElementById('editarProdutoModal'));
    const salvarEdicaoBtn = document.getElementById('salvarEdicao');

    // Variáveis de estado
    let produtos = [];

    // Inicialização
    carregarProdutos();

    // Event Listeners
    produtoForm.addEventListener('submit', handleAdicionarProduto);
    salvarEdicaoBtn.addEventListener('click', handleSalvarEdicao);

    // Funções
    async function carregarProdutos() {
        try {
            produtos = await EstoqueAPI.listarProdutos();
            renderizarProdutos();
            calcularValorTotalEstoque();
        } catch (error) {
            mostrarErro(error.message);
        }
    }

    function renderizarProdutos() {
        tabelaProdutos.innerHTML = produtos.map(produto => `
            <tr data-id="${produto.id}">
                <td>${produto.id}</td>
                <td>${produto.nome}</td>
                <td>R$ ${produto.preco.toFixed(2)}</td>
                <td>${produto.quantidade}</td>
                <td>R$ ${produto.calcularTotal().toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary btn-action" onclick="editarProduto(${produto.id})">
                        <i class="bi bi-pencil"></i> Editar
                    </button>
                    <button class="btn btn-sm btn-outline-success btn-action" onclick="adicionarEstoque(${produto.id}, 1)">
                        <i class="bi bi-plus"></i> +1
                    </button>
                    <button class="btn btn-sm btn-outline-warning btn-action" onclick="removerEstoque(${produto.id}, 1)">
                        <i class="bi bi-dash"></i> -1
                    </button>
                    <button class="btn btn-sm btn-outline-danger btn-action" onclick="removerProduto(${produto.id})">
                        <i class="bi bi-trash"></i> Remover
                    </button>
                </td>
            </tr>
        `).join('');
    }

    async function handleAdicionarProduto(e) {
        e.preventDefault();
        
        const nome = document.getElementById('nome').value;
        const preco = document.getElementById('preco').value;
        const quantidade = document.getElementById('quantidade').value;

        try {
            const novoProduto = new Produto(null, nome, preco, quantidade);
            const produtoCriado = await EstoqueAPI.adicionarProduto(novoProduto);
            produtos.push(Produto.fromJSON(produtoCriado));
            renderizarProdutos();
            calcularValorTotalEstoque();
            produtoForm.reset();
        } catch (error) {
            mostrarErro(error.message);
        }
    }

    function calcularValorTotalEstoque() {
        const total = produtos.reduce((sum, produto) => sum + produto.calcularTotal(), 0);
        valorTotalEstoque.textContent = total.toFixed(2);
    }

    // Funções globais (acessíveis no onclick)
    window.editarProduto = async function(id) {
        const produto = produtos.find(p => p.id === id);
        document.getElementById('editarId').value = produto.id;
        document.getElementById('editarNome').value = produto.nome;
        document.getElementById('editarPreco').value = produto.preco;
        document.getElementById('editarQuantidade').value = produto.quantidade;
        editarProdutoModal.show();
    };

    window.adicionarEstoque = async function(id, quantidade) {
        try {
            await EstoqueAPI.adicionarEstoque(id, quantidade);
            await carregarProdutos();
        } catch (error) {
            mostrarErro(error.message);
        }
    };

    window.removerEstoque = async function(id, quantidade) {
        try {
            await EstoqueAPI.removerEstoque(id, quantidade);
            await carregarProdutos();
        } catch (error) {
            mostrarErro(error.message);
        }
    };

    window.removerProduto = async function(id) {
        if (confirm('Tem certeza que deseja remover este produto?')) {
            try {
                await EstoqueAPI.removerProduto(id);
                produtos = produtos.filter(p => p.id !== id);
                renderizarProdutos();
                calcularValorTotalEstoque();
            } catch (error) {
                mostrarErro(error.message);
            }
        }
    };

    async function handleSalvarEdicao() {
        const id = parseInt(document.getElementById('editarId').value);
        const nome = document.getElementById('editarNome').value;
        const preco = parseFloat(document.getElementById('editarPreco').value);
        const quantidade = parseInt(document.getElementById('editarQuantidade').value);

        try {
            const produtoAtualizado = new Produto(id, nome, preco, quantidade);
            await EstoqueAPI.atualizarProduto(id, produtoAtualizado);
            const index = produtos.findIndex(p => p.id === id);
            produtos[index] = produtoAtualizado;
            renderizarProdutos();
            calcularValorTotalEstoque();
            editarProdutoModal.hide();
        } catch (error) {
            mostrarErro(error.message);
        }
    }

    function mostrarErro(mensagem) {
        alert(`Erro: ${mensagem}`);
    }
});