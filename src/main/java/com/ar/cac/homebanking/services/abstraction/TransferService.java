package com.ar.cac.homebanking.services.abstraction;

import com.ar.cac.homebanking.models.dtos.TransferDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransferService {

    Optional<TransferDTO> performTransfer (TransferDTO transferDTO);

    Optional<TransferDTO> getTransferById (Long id);

    Optional<List<TransferDTO>> getTransfers ();

    String deleteTransfer (Long id);

    Optional<TransferDTO> updateTransfer (Long id, TransferDTO transferDto);
}
