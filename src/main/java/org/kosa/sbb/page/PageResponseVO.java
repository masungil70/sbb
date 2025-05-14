package org.kosa.sbb.page;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

/*
 * 상속 : 재사용성 
 *        확장
 *        
 * 기법 : 상속, 포함
 *         
 * */

//VO : value object
@Getter
public class PageResponseVO <T> implements Iterable<T> {
	private Page<T> page; 

	private final int startPage; //페이지 네비게이션 바의 시작 페이지번호 
	private final int endPage;   //페이지 네비게이션 바의 끝 페이지번호
	
	public PageResponseVO(Page<T> page) {

		final int totalPage =  (int)Math.ceil((double)page.getTotalElements() / page.getSize()) - 1;
		
		this.page = page; 
		this.startPage = ((page.getNumber()) / 10) * 10;
		this.endPage = ((page.getNumber()) / 10) * 10 + 9 > totalPage ? totalPage : ((page.getNumber()) / 10) * 10 + 9;

	}
	
	public long getTotalElements() {
		return page.getTotalElements();
	} 
	public int getTotalPages() {
		return page.getTotalPages();
	}

	public int getNumber() {
		return page.getNumber();
	}

	public int getSize() {
		return page.getSize(); 
	}

	public boolean isPrevious() {
		return page.hasPrevious(); 
	}
	
	public boolean hasPrevious() {
		return page.hasPrevious();
	}
	
	public boolean isNext() {
		return page.hasNext();
	}
	
	public boolean hasNext() {
		return page.hasNext();
	}
	
	public boolean isEmpty() {
		return page.isEmpty(); 
	}
	
	public List<T> getContent() {
		return page.getContent();
	}

	public long getIndexNumber(int index) {
		return page.getTotalElements() - (page.getNumber() * page.getSize()) - index;
	}
	
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return getContent().iterator();
	}
}
