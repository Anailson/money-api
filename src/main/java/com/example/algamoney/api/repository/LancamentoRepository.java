package com.example.algamoney.api.repository;

import org.springframework.stereotype.Repository;

import com.example.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

@Repository
//public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{
public interface LancamentoRepository extends LancamentoRepositoryQuery{

}
