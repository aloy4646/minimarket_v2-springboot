package com.appschef.intern.minimarket.service;

import com.appschef.intern.minimarket.dto.request.promo.AddPromoRequest;
import com.appschef.intern.minimarket.dto.response.promo.PromoDetailResponse;
import com.appschef.intern.minimarket.entity.Product;
import com.appschef.intern.minimarket.entity.Promo;
import com.appschef.intern.minimarket.enumMessage.ProductErrorMessage;
import com.appschef.intern.minimarket.enumMessage.PromoErrorMessage;
import com.appschef.intern.minimarket.enumMessage.ErrorDateMessage;
import com.appschef.intern.minimarket.enumMessage.PromoType;
import com.appschef.intern.minimarket.mapper.PromoMapper;
import com.appschef.intern.minimarket.repository.ProductRepository;
import com.appschef.intern.minimarket.repository.PromoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PromoService {
    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public PromoDetailResponse addPromo(AddPromoRequest addPromoRequest) throws ParseException {
        //pengecekan kodePromo duplikat
        if(promoRepository.existsByPromoCode(addPromoRequest.getPromoCode())){
            throw new ValidationException(PromoErrorMessage.EXIST.getMessage());
        }

        Product product = productRepository.findByProductCodeAndIsDeletedFalse(addPromoRequest.getProductCode())
                .orElseThrow(() -> new ValidationException(ProductErrorMessage.NOT_FOUND.getMessage()));

        cekJenisDanNilaiPromo(addPromoRequest.getType(), addPromoRequest.getValue());

        Date startDate = parseDate(addPromoRequest.getStartDate());
        Date endDate = parseDate(addPromoRequest.getEndDate());
        endDate = setTimeToEndOfDay(endDate);

        if (startDate.after(endDate)) {
            throw new ValidationException(ErrorDateMessage.INVALID_END_DATE.getMessage());
        }

        Promo promo = PromoMapper.mapToPromo(addPromoRequest);
        promo.setProduct(product);
        promo.setStartDate(startDate);
        promo.setEndDate(endDate);

        promoRepository.saveAndFlush(promo);
        return PromoMapper.mapToPromoDetailResponse(promo);
    }

    public PromoDetailResponse getPromoByPromoCode(String kodePromo) {
        Promo promo = promoRepository.findByPromoCode(kodePromo)
                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));

        return PromoMapper.mapToPromoDetailResponse(promo);
    }

    public Page<PromoDetailResponse> getAllPromo(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "promoName"));
        Page<Promo> listPromo = promoRepository.findAll(pageable);
        return listPromo.map(PromoMapper::mapToPromoDetailResponse);
    }

//    @Transactional
//    public DetailPromoResponse updatePromo(String kodePromo, UpdatePromoRequest newPromo) throws ParseException {
//        Promo oldPromo = promoRepository.findByPromoCodeAndIsDeletedFalse(kodePromo)
//                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));
//
//        Produk produkBaru = produkRepository.findByProductKodeAndIsDeletedFalse(newPromo.getProductKode())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ProdukErrorMessage.NOT_FOUND.getMessage()));
//
//        cekJenisDanNilaiPromo(newPromo.getJenis(), newPromo.getNilai());
//
//        Date tanggalMulaiBaru = parseDate(newPromo.getTanggalMulai());
//        Date tanggalBerakhirBaru = parseDate(newPromo.getTanggalBerakhir());
//        tanggalBerakhirBaru = setTimeToEndOfDay(tanggalBerakhirBaru);
//
//        oldPromo.setNamaPromo(newPromo.getNamaPromo());
//        oldPromo.setJenis(newPromo.getJenis());
//        oldPromo.setNilai(newPromo.getNilai());
//        oldPromo.setTanggalMulai(tanggalMulaiBaru);
//        oldPromo.setTanggalBerakhir(tanggalBerakhirBaru);
//        oldPromo.setProduk(produkBaru);
//
//        promoRepository.saveAndFlush(oldPromo);
//
//        return PromoMapper.mapToDetailPromoResponse(oldPromo);
//    }

//    @Transactional
//    public void deletePromo(String kodePromo) {
//        Long idPromo = promoRepository.findIdByPromoCodeAndIsDeletedFalse(kodePromo)
//                .orElseThrow(() -> new ValidationException(PromoErrorMessage.NOT_FOUND.getMessage()));
//
//        int affectedRows = promoRepository.softDeleteById(idPromo);
//        if (affectedRows == 0){
//            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, PromoErrorMessage.GAGAL_HAPUS.getMessage());
//        }
//    }

    private Date parseDate(String tanggal) throws ParseException {
        if (tanggal == null || tanggal.isEmpty()) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(tanggal);
    }

    private Date setTimeToEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private void cekJenisDanNilaiPromo(String jenis, BigDecimal nilai){
        if(!Objects.equals(jenis, PromoType.PERCENT.getType()) && !Objects.equals(jenis, PromoType.FLAT_AMOUNT.getType())){
            throw new ValidationException(PromoErrorMessage.JENIS_INVALID.getMessage());
        }else if (Objects.equals(jenis, PromoType.PERCENT.getType()) && nilai.compareTo(new BigDecimal("1.00")) > 0){
            throw new ValidationException(PromoErrorMessage.NILAI_INVALID.getMessage());
        }
    }


}
